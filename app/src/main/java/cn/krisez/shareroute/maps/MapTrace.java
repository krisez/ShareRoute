package cn.krisez.shareroute.maps;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.krisez.kotlin.net.API;
import cn.krisez.kotlin.ui.views.IMapView;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.R;
import cn.krisez.shareroute.bean.TrackPoint;

class MapTrace {

    private static MapTrace INSTANCE;

    private Polyline mPolyline;
    private List<TrackPoint> mList;

    private AMap mAMap;
    private IMapView mMapView;

    private MapTrace() {
    }

    public static MapTrace INSTANCE() {
        //先检查实例是否存在，如果不存在才进入下面的同步块
        if (INSTANCE == null) {
            synchronized (MapTrace.class) {
                //再次检查实例是否存在，如果不存在才真正的创建实例
                if (INSTANCE == null) {
                    INSTANCE = new MapTrace();
                }
            }
        }

        return INSTANCE;
    }

    public void init(MapView mapView, IMapView view){
        this.mAMap = mapView.getMap();
        mList = new ArrayList<>();
        this.mMapView = view;
    }

    /**
     * @param id 用户id
     * @param start 开始时间
     * @param end 结束时间
     */
    public void startTrace(String id,String start,String end) {
        mList.clear();
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).getTracePoints(id,start,end))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        mList.addAll(new Gson().fromJson(result.extra,new TypeToken<List<TrackPoint>>(){}.getType()));
                        Log.d("MapTrace", "onSuccess:" + mList.size());
                        addPolylineInPlayGround(mList);
                    }

                    @Override
                    public void onFailed(String s) {
                        mMapView.showTips(s);
                    }
                });
    }

    public Polyline getPolyline() {
        return mPolyline;
    }

    /**
     * 添加轨迹线
     *
     * @param points
     */
    private void addPolylineInPlayGround(List<TrackPoint> points) {
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            list.add(new LatLng(points.get(i).getLat(), points.get(i).getLng()));
        }
        List<Integer> colorList = new ArrayList<>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.custtexture));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = mAMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(18));

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (int i = 0; i < list.size(); i++) {
            builder.include(list.get(i));
        }
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));
        mMapView.overTrace();
    }

    /**
     * 动画效果
     */
    public Animator setAnimation(final Marker marker, float duration) {
        ValueAnimator animator;
        Object[] objects = new Object[mList.size()];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = mList.get(i);
        }
        animator = ValueAnimator.ofObject((TypeEvaluator<TrackPoint>) (fraction, startValue, endValue) -> {
            double lat = startValue.getLat() + fraction * (endValue.getLat() - startValue.getLat());
            double lng = startValue.getLng() + fraction * (endValue.getLng() - startValue.getLng());
            double speed = startValue.getSpeed() + fraction * (endValue.getSpeed() - startValue.getSpeed());
            return new TrackPoint(lng, lat, speed, getAngle(startValue.getLatLng(), endValue.getLatLng()));
        }, objects);

        animator.addUpdateListener(animation -> {
            TrackPoint carRoute = (TrackPoint) animation.getAnimatedValue();
            marker.setMarkerOptions(marker.getOptions().position(carRoute.getLatLng()).rotateAngle(carRoute.getDirection()));
            //默认无
            //  mAMap.moveCamera(CameraUpdateFactory.changeLatLng(carRoute.getLatLng()));
        });
//        animator.setInterpolator(new SpeedInterpolator(mList, duration));
        animator.setDuration((long) duration);
        animator.start();
        //return animator;
        return null;
    }

    private float getAngle(LatLng latLng1, LatLng latLng2) {
        if (latLng1 == null || latLng2 == null)
            return 0;
        float angle = 0;
        if (latLng2.longitude - latLng1.longitude == 0) {
            angle = latLng1.latitude > latLng2.latitude ? 180 : 0;
        } else if (latLng2.latitude - latLng1.latitude == 0) {
            angle = latLng1.longitude < latLng2.longitude ? 90 : 270;
        } else {
            double a = (latLng2.latitude - latLng1.latitude) / (latLng2.longitude - latLng1.longitude);
            if (latLng2.longitude > latLng1.longitude) {
                angle = (float) (Math.atan(a) * 180 / Math.PI) - 90;
            } else {
                angle = (float) (Math.atan(a) * 180 / Math.PI) + 90;
            }
        }
        return angle;
    }

    public void removeLine() {
        if (getPolyline() != null) {
            getPolyline().remove();
        }
    }

    public void destroy() {
        INSTANCE = null;
    }
}
