package cn.krisez.imchat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.krisez.network.RequestSubscribe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.krisez.framework.base.BaseActivity;
import cn.krisez.framework.base.Presenter;
import cn.krisez.framework.utils.DensityUtil;
import cn.krisez.imchat.R;
import cn.krisez.imchat.adapter.ChatAdapter;
import cn.krisez.imchat.bean.ChatTypeBean;
import cn.krisez.imchat.bean.MessageBean;
import cn.krisez.imchat.client.WebSocketTransfer;
import cn.krisez.imchat.db.IMMsgRxDbManager;
import cn.krisez.imchat.manager.MessageManager;
import cn.krisez.imchat.presneter.ChatPresenter;
import cn.krisez.imchat.ui.IChatView;
import cn.krisez.imchat.utils.SharePreferenceUtils;
import io.reactivex.functions.Consumer;

public class ChatActivity extends BaseActivity implements IChatView {
    private ChatPresenter mPresenter;
    private ChatAdapter mAdapter;
    private List<ChatTypeBean> mList = new ArrayList<>();
    private String friendName;
    private String friendId;

    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Button mButton;

    public static void chatStart(Context context, String msg, String friendId, String name) {
        Intent intent = new Intent(context, ChatActivity.class);
        if ("".equals(msg)) {
            msg = new Gson().toJson(new ArrayList<>());
        }
        intent.putExtra("msgs", msg);
        intent.putExtra("friendId", friendId);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    @Override
    protected View newView() {
        return View.inflate(this, R.layout.activity_chat, null);
    }

    @Override
    protected Presenter presenter() {
        return mPresenter = new ChatPresenter(this, this);
    }

    @Override
    protected void init(Bundle bundle) {
        initData();
        initView();
        initListener();
    }

    private void initView() {
        setUpToolbar();
        setTitle(friendName);
        showBackIconAndClick();
        setRefreshEnable(false);
        mAdapter = new ChatAdapter(mList, this);
        mRecyclerView = findViewById(R.id.msg_recycler);
        mEditText = findViewById(R.id.chat_send_context);
        mButton = findViewById(R.id.chat_send_btn);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        manager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initData() {
        friendName = getIntent().getStringExtra("name");
        friendId = getIntent().getStringExtra("friendId");
        String data = getIntent().getStringExtra("msgs");
        List<MessageBean> list = new Gson().fromJson(data, new TypeToken<List<MessageBean>>() {
        }.getType());
        for (int i = 0; i < list.size(); i++) {
            mList.add(new ChatTypeBean(list.get(i), ChatTypeBean.TYPE_TEXT));
        }
    }

    private void initListener() {
        mEditText.setOnClickListener(v -> new Handler().postDelayed(() -> runOnUiThread(() -> mRecyclerView.scrollToPosition(mList.size() - 1)), 100));
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("ChatActivity", "onTextChanged:" + s);
                String text = s.toString();
                if (!text.isEmpty()) {
                    mButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    mButton.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mButton.setOnClickListener(v -> {
            String text = mEditText.getText().toString();
            if (!text.isEmpty()) {
                MessageBean msg = new MessageBean();
                msg.from = SharePreferenceUtils.obj(this).getUserId();
                msg.to = friendId;
                msg.type = "0";
                msg.content = text;
                msg.time = DensityUtil.getTime();
                MessageManager.send(new Gson().toJson(new WebSocketTransfer(0, msg.toString())));
                mEditText.setText("");
                mButton.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                IMMsgRxDbManager.getInstance(this).insertMsg(msg).subscribe(b -> addMsg(msg));
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void insert(MessageBean messageBean) {
        runOnUiThread(() -> {
            addMsg(messageBean);
        });
    }

    private void addMsg(MessageBean msg) {
        mList.add(new ChatTypeBean(msg, ChatTypeBean.TYPE_TEXT));
        mAdapter.notifyItemChanged(mList.size());
        mRecyclerView.scrollToPosition(mList.size() - 1);
    }
}
