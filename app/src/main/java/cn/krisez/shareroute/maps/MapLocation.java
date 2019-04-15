package cn.krisez.shareroute.maps;

import android.content.Context;
import android.util.Log;

import cn.krisez.framework.utils.DensityUtil;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.greenrobot.eventbus.EventBus;

import cn.krisez.kotlin.net.API;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.event.LocationEvent;
import cn.krisez.shareroute.event.MyLocationEvent;
import cn.krisez.shareroute.utils.Const;
import cn.krisez.shareroute.utils.SPUtil;

public class MapLocation implements AMapLocationListener {
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;

    void startLocate(Context context) {
        mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5 * 1000);
        //传感器加载
        mLocationOption.setSensorEnable(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //后台定位
//        mlocationClient.enableBackgroundLocation(1,new Notification());
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        String lat = String.valueOf(aMapLocation.getLatitude());
        String lng = String.valueOf(aMapLocation.getLongitude());
        String speed = String.valueOf(aMapLocation.getSpeed() * 3.6f);
        String bearing = String.valueOf(aMapLocation.getBearing());
        String addr = aMapLocation.getAddress();
        EventBus.getDefault().post(new MyLocationEvent(lat, lng, speed, bearing, addr));
        if (Const.uploadLocation) {
            NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).postPos(SPUtil.getUser().id, lat, lng, speed, bearing, aMapLocation.getStreet(), DensityUtil.getTime()))
                    .handler(new ResultHandler() {
                        @Override
                        public void onSuccess(Result result) {
                        }

                        @Override
                        public void onFailed(String msg) {

                        }
                    });
        }
    }

    void stopLocation() {
        if (mlocationClient != null) {
//            mlocationClient.disableBackgroundLocation(true);
            mlocationClient.stopLocation();
            mlocationClient.unRegisterLocationListener(this);
            mlocationClient.onDestroy();

        }
    }
}
