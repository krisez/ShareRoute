package cn.krisez.imchat.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;

import java.util.List;

import cn.krisez.imchat.bean.ChatTypeBean;

public class ChatAdapter extends MultipleItemRvAdapter<ChatTypeBean, BaseViewHolder> {
    public ChatAdapter(@Nullable List<ChatTypeBean> data) {
        super(data);
    }

    @Override
    protected int getViewType(ChatTypeBean chatTypeBean) {
        return 0;
    }

    @Override
    public void registerItemProvider() {

    }
}
