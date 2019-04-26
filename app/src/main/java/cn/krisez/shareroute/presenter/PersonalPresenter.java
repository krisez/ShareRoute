package cn.krisez.shareroute.presenter;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import cn.krisez.framework.base.IBaseView;
import cn.krisez.framework.base.Presenter;
import cn.krisez.framework.utils.DensityUtil;
import cn.krisez.kotlin.net.API;
import cn.krisez.kotlin.ui.views.IPersonView;
import cn.krisez.network.NetWorkUtils;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.bean.User;
import cn.krisez.shareroute.utils.SPUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PersonalPresenter extends Presenter {

    private IPersonView mView;

    public PersonalPresenter(IBaseView view, Context context) {
        super(view, context);
        this.mView = (IPersonView) view;
    }

    @Override
    public void onCreate() {

    }

    public void upLoad2Server(String path) {
        File file = new File(DensityUtil.compressImage(path, Environment.getExternalStorageDirectory() + "/随行/imgs/" + System.currentTimeMillis() + ".png", 10));
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("mFile", file.getName(), requestBody);
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).uploadFile(SPUtil.getUser().id, body))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        User user = SPUtil.getUser();
                        user.avatar = result.extra;
                        SPUtil.saveUser(user);
                        mView.uploadHead(result.extra);
                    }

                    @Override
                    public void onFailed(String s) {
                        mView.error(s);
                    }
                });
    }

    public void sendSMDCode(String mobile) {
        NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).sendSMS(mobile))
                .handler(new ResultHandler() {
                    @Override
                    public void onSuccess(Result result) {
                        mView.code(result.extra);
                    }

                    @Override
                    public void onFailed(String s) {

                    }
                });
    }
}
