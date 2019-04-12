package cn.krisez.imchat.bean;

import com.chad.library.adapter.base.entity.SectionMultiEntity;

public class AddFriendSection extends SectionMultiEntity<UserBean> {
    private int type;

    public AddFriendSection(UserBean userBean,int type) {
        super(userBean);
        this.type = type;
    }

    public AddFriendSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    @Override
    public int getItemType() {
        return type;
    }
}
