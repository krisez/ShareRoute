package cn.krisez.shareroute.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.krisez.shareroute.R;
import cn.krisez.shareroute.bean.SysMailBean;

public class NotifyAdapter extends BaseQuickAdapter<SysMailBean, BaseViewHolder> {
    public NotifyAdapter(int layoutResId, @Nullable List<SysMailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SysMailBean sysMailBean) {
        baseViewHolder.setText(R.id.item_mail_title,sysMailBean.title);
        baseViewHolder.setText(R.id.item_mail_time,sysMailBean.time);
        baseViewHolder.setText(R.id.item_mail_content,sysMailBean.content);
    }
}
