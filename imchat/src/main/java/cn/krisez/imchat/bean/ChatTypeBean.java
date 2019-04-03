package cn.krisez.imchat.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ChatTypeBean implements MultiItemEntity {
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_VOICE = 2;
    public static final int TYPE_IMG = 3;
    public static final int TYPE_FILE = 4;
    public static final int TYPE_ADDRESS = 5;

    public MessageBean msg;
    public int type;

    public ChatTypeBean(MessageBean msg, int type) {
        this.msg = msg;
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
