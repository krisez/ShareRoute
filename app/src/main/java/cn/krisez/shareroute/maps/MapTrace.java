package cn.krisez.shareroute.maps;

import android.animation.Animator;
import android.animation.ValueAnimator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;

class MapTrace {

    private static MapTrace INSTANCE;

    private Polyline mPolyline;
    //private List<CarRoute> mList;

    private AMap mAMap;
   // private IMainView mIMainView;

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

    public void init(MapView mapView){
        this.mAMap = mapView.getMap();
       // mList = new ArrayList<>();
        //this.mIMainView = view;
    }

    public void startTrace(String id) {
       // mList.clear();
    }

    public Polyline getPolyline() {
        return mPolyline;
    }

    /**
     * 添加轨迹线
     *
     * @param points
     *//*
    private void addPolylineInPlayGround(List<CarRoute> points) {
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            list.add(new LatLng(points.get(i).getLat(), points.get(i).getLon()));
        }
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        //textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.custtexture));

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

        // LatLng latLng = new LatLng((list.get(0).latitude+list.get(list.size()-1).latitude)/2,
        //    (list.get(0).longitude+list.get(list.size()-1).longitude)/2);

        //LatLngBounds bounds = new LatLngBounds(queryPoints(list,1), queryPoints(list,2));
        LatLngBounds.Builder builder = LatLngBounds.builder();

        for (int i = 0; i < list.size(); i++) {
            builder.include(list.get(i));
        }
//
//        Log.d("MapTrace", "addPolylineInPlayGround:" + bounds.northeast);
//        Log.d("MapTrace", "addPolylineInPlayGround:" + bounds.southwest);
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));
        //mIMainView.traceOver();
    }*/

    /**
     * 动画效果
     */
    public Animator setAnimation(final Marker marker, float duration) {
        ValueAnimator animator;
        /*Object[] objects = new Object[mList.size()];
        for (int i = 0; i < objects.length; i++) {
            //objects[i] = mList.get(i);
        }*/
        /*animator = ValueAnimator.ofObject((TypeEvaluator<CarRoute>) (fraction, startValue, endValue) -> {
            double lat = startValue.getLat() + fraction * (endValue.getLat() - startValue.getLat());
            double lon = startValue.getLon() + fraction * (endValue.getLon() - startValue.getLon());
            double speed = startValue.getSpeed() + fraction * (endValue.getSpeed() - startValue.getSpeed());
            return new CarRoute(lon, lat, speed, getAngle(startValue.getLatLng(), endValue.getLatLng()));
        }, objects);

        animator.addUpdateListener(animation -> {
            CarRoute carRoute = (CarRoute) animation.getAnimatedValue();
            marker.setMarkerOptions(marker.getOptions().position(carRoute.getLatLng()).rotateAngle((float) carRoute.getBearing()));
            //默认无
            //  mAMap.moveCamera(CameraUpdateFactory.changeLatLng(carRoute.getLatLng()));
        });
        animator.setInterpolator(new SpeedInterpolator(mList, duration));
        animator.setDuration((long) duration);
        animator.start();*/
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
