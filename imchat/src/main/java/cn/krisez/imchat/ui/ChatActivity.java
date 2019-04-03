package cn.krisez.imchat.ui;

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
import cn.krisez.imchat.db.IMMsgRxDbManager;
import cn.krisez.imchat.manager.MessageManager;
import cn.krisez.imchat.presneter.ChatPresenter;
import cn.krisez.imchat.utils.SharePreferenceUtils;

public class ChatActivity extends BaseActivity implements IChatView {
    private ChatPresenter mPresenter;
    private ChatAdapter mAdapter;
    private List<ChatTypeBean> mList = new ArrayList<>();
    private String friendName;
    private String friendId;

    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Button mButton;

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
        mEditText.setOnClickListener(v-> new Handler().postDelayed(() -> runOnUiThread(() -> mRecyclerView.scrollToPosition(mList.size()-1)),100));
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != "") {
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
                MessageManager.send(new Gson().toJson(msg));
                mEditText.setText("");
                mButton.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                addMsg(msg);
                IMMsgRxDbManager.getInstance(this).insertMsg(msg);
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void insert(MessageBean messageBean) {
        addMsg(messageBean);
    }

    private void addMsg(MessageBean msg) {
        runOnUiThread(() -> {
            mList.add(new ChatTypeBean(msg, ChatTypeBean.TYPE_TEXT));
            mAdapter.notifyItemChanged(mList.size());
            mRecyclerView.scrollToPosition(mList.size() - 1);
        });
    }
}
