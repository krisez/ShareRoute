package cn.krisez.imchat.net;

import cn.krisez.network.bean.Result;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("getChatList.m")
    Observable<Result> chatList(@Query("userId")String id,@Query("time")String time,@Query("msgId")String msgId);

}
