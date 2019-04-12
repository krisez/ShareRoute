package cn.krisez.imchat.ui;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.imchat.bean.UserBean;
import cn.krisez.imchat.bean.MessageBean;

import java.util.List;
import java.util.Map;

public interface IIMView extends IBaseView {
    void showTips(String tips);
    void chatList(Map<String, List<MessageBean>> map);
    void getFriendsList(List<UserBean> list,int type);//加载好友列表以及查找用户
}
