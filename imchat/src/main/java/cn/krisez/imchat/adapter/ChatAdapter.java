package cn.krisez.imchat.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.krisez.framework.utils.DensityUtil;
import cn.krisez.framework.widget.CircleImageView;
import cn.krisez.imchat.R;
import cn.krisez.imchat.bean.ChatTypeBean;
import cn.krisez.imchat.utils.SharePreferenceUtils;

public class ChatAdapter extends BaseMultiItemQuickAdapter<ChatTypeBean, BaseViewHolder> {
    private Context mContext;

    public ChatAdapter(@Nullable List<ChatTypeBean> data, Context context) {
        super(data);
        this.mContext = context;
        addItemType(ChatTypeBean.TYPE_TEXT, R.layout.item_chat_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatTypeBean item) {
        if (item.msg.from.equals(SharePreferenceUtils.obj(mContext).getUserId())) {
            helper.setGone(R.id.chat_msg_left, false);
            helper.setGone(R.id.chat_msg_right, true);
            TextView context = helper.getView(R.id.chat_msg_right_content);
            context.setText(item.msg.content);
            context.setMaxWidth(DensityUtil.getParentWidth(mContext)/3*2);
            CircleImageView civ = helper.getView(R.id.chat_msg_right_avatar);
            Glide.with(mContext).setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_icon).placeholder(R.drawable.ic_icon)).load(SharePreferenceUtils.obj(mContext).getHearUrl()).into(civ);
        } else {
            helper.setGone(R.id.chat_msg_right, false);
            helper.setGone(R.id.chat_msg_left, true);
            TextView context = helper.getView(R.id.chat_msg_left_content);
            context.setText(item.msg.content);
            context.setMaxWidth(DensityUtil.getParentWidth(mContext)/3*2);
            CircleImageView civ = helper.getView(R.id.chat_msg_left_avatar);
            Glide.with(mContext).setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_icon).placeholder(R.drawable.ic_icon)).load(item.msg.headUrl).into(civ);
        }
    }
}
