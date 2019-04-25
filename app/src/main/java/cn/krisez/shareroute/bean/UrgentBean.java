package cn.krisez.shareroute.bean;

public class UrgentBean {
    public String mobile;
    public String realName;
    public String userId;

    public UrgentBean(String userId,String mobile, String realName) {
        this.userId = userId;
        this.mobile = mobile;
        this.realName = realName;
    }
}
