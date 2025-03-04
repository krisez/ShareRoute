package cn.krisez.shareroute.maps;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.krisez.framework.utils.DensityUtil;
import cn.krisez.kotlin.net.API;
import cn.krisez.kotlin.ui.views.IMapView;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.R;
import cn.krisez.shareroute.bean.TrackPoint;
import cn.krisez.shareroute.event.MyLocationEvent;

public class MapController /*implements AMapLocationListener, LocationSource*/ {
    private Context mContext;

    private MapView mMapView;
    private TextureMapView mTextureMapView;
    private AMap mMap;
    private Marker mMyMarker;
    private MapLocation mapLocation;
    private IMapView mView;

    public MapController(Context context) {
        this.mContext = context;
    }

    public MapController with(Context context) {
        this.mContext = context;
        return this;
    }

    public MapController map(MapView mapView) {
        this.mMapView = mapView;
        this.mMap = mapView.getMap();
        return this;
    }

    public MapController map(TextureMapView mapView) {
        this.mTextureMapView = mapView;
        this.mMap = mapView.getMap();
        return this;
    }

    public MapController view(IMapView view) {
        this.mView = view;
        return this;
    }

    public MapController defaultAmap() {
        if (mapLocation == null) {
            mapLocation = new MapLocation();
            mapLocation.startLocate(mContext);
        }

        EventBus.getDefault().register(this);

        mMap.getUiSettings().setCompassEnabled(true);//指南针
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f));
        mMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                isCenter = false;
            }
        });
        return this;
    }

    private boolean isCenter = true;
    private double mLat;
    private double mLng;
    //    private float mBearing;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMyLocationEvent(MyLocationEvent event) {
        if (mMyMarker == null) {
            mMyMarker = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.w))
                    .position(new LatLng(event.getLat(), event.getLng())).title("自己").setFlat(true).rotateAngle(-event.getBearing()));
        }
        mMyMarker.setPosition(new LatLng(mLat = event.getLat(), mLng = event.getLng()));
        mMyMarker.setRotateAngle(-event.getBearing());
        //开启定位
        if (isCenter) {
            mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(event.getLat(), event.getLng())));
        }
    }

    //定位自己的位置
    public void locateMyPosition() {
        isCenter = true;
        mMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(mLat, mLng)));
    }

    public void noCenter() {
        isCenter = false;
    }

    /**
     * marker设置处
     */
    private Marker mMarker;

    public MapController setMarkerOption(MarkerOptions option) {
        mMarker = new MapMarker(mMapView).getMarker(option);
        return this;
    }

    public Marker getMarker() {
        return mMarker;
    }

    /**
     * trace 设置处
     */
    public MapController setTrace(String id, String start, String end) {
        MapTrace.INSTANCE().init(mMapView, mView);
        clearTrace();
        MapTrace.INSTANCE().startTrace(id, start, end);
        return this;
    }

    Animator mAnimator;

    public Animator getMarkerAnimator(float duration) {
        return mAnimator = MapTrace.INSTANCE().setAnimation(mMarker, duration);
    }

    public List<LatLng> getTracePoints() {
        return MapTrace.INSTANCE().getPolyline().getPoints();
    }

    public MapController clearTrace() {
        MapTrace.INSTANCE().removeLine();
        return this;
    }

    /**
     * @param type    0,求助码；1，id
     * @param content
     */
    public void startNewHelp(int type, String content) {
        MapTrace.INSTANCE().init(mMapView, mView);
        clearTrace();
        time = DensityUtil.getTime();
        this.type = type;
        this.content = content;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 0, 5000);
    }

    private String time = "";
    private int type = 0;
    private String content = "";
    private Handler mHandler = new Handler(msg -> {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).realTimeGps(type, content, time))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        List<LatLng> list = MapTrace.INSTANCE().getPolyline().getPoints();
                        TrackPoint point = new Gson().fromJson(result.extra, TrackPoint.class);
                        time = point.createTime;
                        LatLng latLng = new LatLng(point.getLat(), point.getLng());
                        list.add(latLng);
                        MapTrace.INSTANCE().getPolyline().setPoints(list);
                        if (mMarker != null) {
                            mMarker.setMarkerOptions(mMarker.getOptions().position(latLng).rotateAngle(Float.parseFloat(point.direction)));
                        } else {
                            setMarkerOption(new MarkerOptions().title(point.userId).rotateAngle(Float.parseFloat(point.direction)).position(latLng));
                        }
                    }

                    @Override
                    public void onFailed(String s) {
                        Log.e("MapTrack", "onFailed: " + s);
                    }
                });
        return true;
    });


    public void onSaveInstanceState(Bundle bundle) {
        if (mMapView != null) {
            mMapView.onSaveInstanceState(bundle);
        } else {
            if (mTextureMapView != null) {
                mTextureMapView.onSaveInstanceState(bundle);
            }
        }
    }

    public void onResume() {
        if (mMapView != null) {
            mMapView.onResume();
        } else {
            if (mTextureMapView != null) {
                mTextureMapView.onResume();
            }
        }
    }

    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        } else {
            if (mTextureMapView != null) {
                mTextureMapView.onPause();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mAnimator != null) {
            if (mAnimator.isStarted() && mAnimator.isRunning())
                mAnimator.pause();
        }
    }

    public void onDestroy() {
        if (mMapView != null) {
            mMapView.onDestroy();
        } else {
            if (mTextureMapView != null) {
                mTextureMapView.onDestroy();
            }
        }
        if (mContext != null) {
            mContext = null;
        }
        if (mapLocation != null) {
            mapLocation.stopLocation();
        }
        MapTrace.INSTANCE().destroy();
    }

    public void create(Bundle savedInstanceState) {
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
        } else {
            if (mTextureMapView != null) {
                mTextureMapView.onCreate(savedInstanceState);
            }
        }
    }
}
