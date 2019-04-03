package cn.krisez.imchat.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.krisez.framework.widget.CircleImageView;
import cn.krisez.imchat.R;
import cn.krisez.imchat.bean.ConversationBean;


public class ConversationAdapter extends BaseQuickAdapter<ConversationBean, BaseViewHolder> {
    private Context mContext;

    public ConversationAdapter(Context context, int layoutResId, @Nullable List<ConversationBean> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ConversationBean item) {
        helper.setText(R.id.conversation_nick, item.name);
        helper.setText(R.id.conversation_time, item.time);
        helper.setText(R.id.conversation_content, item.lastContent);
        helper.setText(R.id.conversation_num_tv, item.no);
        if(item.no.equals("0")){
            helper.setVisible(R.id.conversation_num_tv,false);
            helper.setVisible(R.id.conversation_num,false);
        }
        CircleImageView civ = helper.getView(R.id.conversation_avatar);
        Glide.with(mContext).setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_icon).placeholder(R.drawable.ic_icon)).load(item.headUrl).into(civ);
    }
}
