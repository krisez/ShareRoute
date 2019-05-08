package cn.krisez.shareroute.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import org.java_websocket.WebSocket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.krisez.imchat.manager.MessageManager;
import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.ResultHandler;
import cn.krisez.shareroute.llistener.DownloadListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static cn.krisez.framework.utils.DensityUtil.getTime;

public class Utils {

    public static String time2Add(String time, String past) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh", "CN"));
        try {
            long now = format.parse(time).getTime();
            long p = (Long.parseLong(past.substring(0, past.length() - 2)) + 1) * 1000 * 60;
            return format.format(new Date(now + p));
        } catch (ParseException e) {
            e.printStackTrace();
            return getTime();
        }
    }

    public static String getAppVersionName(Context context){
        String versionName = null;
        try{
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private static Disposable d;

    public static void writeFile2Disk(ResponseBody response, DownloadListener listener) {
        if (d != null) {
            d.dispose();
        }
        d = Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            long currentLength = 0;
            OutputStream os = null;

            InputStream is = response.byteStream();
            long totalLength = response.contentLength();
            File file = new File(Environment.getExternalStorageDirectory() + "/随行/download/lastVersion.apk");
            try {
                if (!file.exists()) {
                    file.getParentFile().mkdir();
                } else {
                    file.delete();
                }
                os = new FileOutputStream(file);

                int len;

                byte[] buff = new byte[1024];

                while ((len = is.read(buff)) != -1) {
                    os.write(buff, 0, len);
                    currentLength += len;
                    emitter.onNext((int) (currentLength *1.0/ totalLength * 100));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    listener.onProgress(integer);
                    if (integer == 100) {
                        listener.onFinish();
                    }
                });
    }

    public static boolean isConnect(Context context){
        if(MessageManager.instance().getReadyState()== WebSocket.READYSTATE.NOT_YET_CONNECTED){
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }
}
