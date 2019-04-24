package cn.krisez.shareroute.bean;

public class TraceHistoryBean {
    public String startAddr;
    public String endAddr;
    public String createTime;
    public double distance;
    public String pasTime;

    public TraceHistoryBean() {
    }

    public TraceHistoryBean(String startAddr, String endAddr, String createTime, double distance, String pasTime) {
        this.startAddr = startAddr;
        this.endAddr = endAddr;
        this.createTime = createTime;
        this.distance = distance;
        this.pasTime = pasTime;
    }
}
