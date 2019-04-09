package cn.krisez.imchat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.krisez.framework.widget.CircleImageView;
import cn.krisez.imchat.R;
import cn.krisez.imchat.bean.UserBean;

public class FriendAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {

    private boolean add = false;
    private Context mContext;

    public FriendAdapter(Context context, int layoutResId, @Nullable List<UserBean> data) {
        super(layoutResId, data);
        if (layoutResId == R.layout.item_add_friend) {
            add = true;
        }
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        helper.setText(R.id.item_friend_uid, item.id);
        helper.setText(R.id.item_friend_nick, item.name);
        CircleImageView circleImageView = helper.getView(R.id.item_friend_avatar);
        Glide.with(helper.itemView).setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_icon).placeholder(R.drawable.ic_icon)).load(item.avatar).into(circleImageView);
        if (add) {
            if (item.request) {
                helper.setText(R.id.request_add_friend_btn, "已发送");
                helper.setBackgroundColor(R.id.request_add_friend_btn, Color.LTGRAY);
                helper.getView(R.id.request_add_friend_btn).setClickable(false);
            } else {
                helper.setText(R.id.request_add_friend_btn, R.string.add_friend);
                helper.setBackgroundColor(R.id.request_add_friend_btn,mContext.getResources().getColor(R.color.colorAccent));
                helper.addOnClickListener(R.id.request_add_friend_btn);
            }
        }
    }
}
