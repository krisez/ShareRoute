package cn.krisez.shareroute.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.kotlin.net.API;
import cn.krisez.kotlin.ui.views.ITraceHistoryView;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.bean.TraceHistoryBean;

public class TraceHistoryPresenter extends Presenter {
    private ITraceHistoryView mView;

    public TraceHistoryPresenter(IBaseView view, Context context) {
        super(view, context);
        this.mView = (ITraceHistoryView) view;
    }

    @Override
    public void onCreate() {

    }

    public void getTraces(String id){
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).getTraceHistory(id))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        mView.traceHistory(new Gson().fromJson(result.extra,new TypeToken<List<TraceHistoryBean>>(){}.getType()));
                    }

                    @Override
                    public void onFailed(String s) {
                        mView.error(s);
                    }
                });
    }
}
