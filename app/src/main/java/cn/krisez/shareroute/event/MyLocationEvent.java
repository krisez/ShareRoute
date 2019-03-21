package cn.krisez.shareroute.event;

public class MyLocationEvent {
    private String lat;
    private String lng;
    private String speed;
    private String bearing;
    public String addr;

    public MyLocationEvent(String addr) {
        this.addr = addr;
    }

    public MyLocationEvent(String lat, String lng, String speed, String bearing, String addr) {
        this.lat = lat;
        this.lng = lng;
        this.speed = speed;
        this.bearing = bearing;
        this.addr = addr;
    }

    public MyLocationEvent(String lat, String lng, String speed, String bearing) {
        this.lat = lat;
        this.lng = lng;
        this.speed = speed;
        this.bearing = bearing;
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

    public float getBearing() {
        return Float.parseFloat(bearing);
    }

    public String getAddr() {
        return addr;
    }
}
