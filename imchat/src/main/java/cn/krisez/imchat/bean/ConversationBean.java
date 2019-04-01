package cn.krisez.imchat.bean;

/**
 * 聊天列表
 */
public class ConversationBean {
    public String fromId;
    public String toId;
    public String headUrl;
    public String name;
    public String lastContent;
    public String time;
    public String no;

    public ConversationBean() {
    }

    public ConversationBean(String fromId, String toId, String headUrl, String name, String lastContent, String time, String no) {
        this.fromId = fromId;
        this.toId = toId;
        this.headUrl = headUrl;
        this.name = name;
        this.lastContent = lastContent;
        this.time = time;
        this.no = no;
    }
}
