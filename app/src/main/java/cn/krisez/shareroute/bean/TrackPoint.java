package cn.krisez.shareroute.bean;

import com.amap.api.maps.model.LatLng;

public class TrackPoint {
    public String userId;
    public String lat;
    public String lng;
    public String speed;
    public String direction;
    public String createTime;

    public TrackPoint(double lng, double lat, double speed, float direction) {
        this.lng = lng+"";
        this.lat = lat+"";
        this.speed = speed+"";
        this.direction = direction+"";
    }

    public double getLat() {
        return Double.parseDouble(lat);
    }

    public double getLng() {
        return Double.parseDouble(lng);
    }

    public float getSpeed() {
        return Float.parseFloat(speed);
    }

    public float getDirection() {
        return Float.parseFloat(direction);
    }

    public LatLng getLatLng() {
        return new LatLng(getLat(),getLng());
    }
}
