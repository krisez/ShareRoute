package cn.krisez.imchat.adapter;

import android.graphics.Color;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseSectionMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.krisez.framework.widget.CircleImageView;
import cn.krisez.imchat.R;
import cn.krisez.imchat.bean.AddFriendSection;

public class AddFriendAdapter extends BaseSectionMultiItemQuickAdapter<AddFriendSection, BaseViewHolder> {

    public AddFriendAdapter(int sectionHeadResId, List<AddFriendSection> data) {
        super(sectionHeadResId, data);
        addItemType(1, R.layout.item_add_friend);
        addItemType(2, R.layout.item_friend_request);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, AddFriendSection item) {
        helper.setText(R.id.im_friend_request,item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddFriendSection item) {
        helper.setText(R.id.item_friend_uid, item.t.id);
        helper.setText(R.id.item_friend_nick, item.t.name);
        CircleImageView circleImageView = helper.getView(R.id.item_friend_avatar);
        Glide.with(helper.itemView).setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_icon).placeholder(R.drawable.ic_icon)).load(item.t.avatar).into(circleImageView);
        switch (helper.getItemViewType()){
            case 1:
                if (item.t.request) {
                    helper.setText(R.id.request_add_friend_btn, "已发送");
                    helper.setBackgroundColor(R.id.request_add_friend_btn, Color.LTGRAY);
                    helper.getView(R.id.request_add_friend_btn).setClickable(false);
                } else {
                    helper.setText(R.id.request_add_friend_btn, R.string.add_friend);
                    helper.setBackgroundColor(R.id.request_add_friend_btn,mContext.getResources().getColor(R.color.colorAccent));
                    helper.addOnClickListener(R.id.request_add_friend_btn);
                }
                break;
            case 2:
                if (item.t.request) {
                    helper.setGone(R.id.friend_request_agree_btn,false);
                    helper.setGone(R.id.friend_request_refuse_btn,false);
                    helper.setGone(R.id.friend_request_deal_btn,true);
                } else {
                    helper.addOnClickListener(R.id.friend_request_agree_btn);
                    helper.addOnClickListener(R.id.friend_request_refuse_btn);
                }
                break;
        }
    }
}
