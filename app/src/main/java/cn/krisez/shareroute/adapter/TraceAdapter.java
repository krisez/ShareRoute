package cn.krisez.shareroute.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.krisez.shareroute.R;
import cn.krisez.shareroute.bean.TraceHistoryBean;

public class TraceAdapter extends BaseQuickAdapter<TraceHistoryBean, BaseViewHolder> {

    public TraceAdapter(int layoutResId, @Nullable List<TraceHistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TraceHistoryBean item) {
        helper.setText(R.id.item_trace_start,item.startAddr);
        helper.setText(R.id.item_trace_end,item.endAddr);
        helper.setText(R.id.item_trace_time,item.createTime);
        helper.setText(R.id.item_trace_distrance,(int)item.distance+"米");
        helper.setText(R.id.item_trace_past,item.pasTime);
    }
}
