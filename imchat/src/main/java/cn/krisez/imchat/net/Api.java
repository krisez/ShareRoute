package cn.krisez.imchat.net;

import cn.krisez.network.bean.Result;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @GET("getChatList.m")
    Observable<Result> chatList(@Query("userId")String id,@Query("time")String time,@Query("msgId")String msgId);
    @POST("allRead.m")
    Observable<Result> updateAllRead(@Query("from")String from,@Query("to")String to);
    @POST("friendsList.m")
    Observable<Result> friends(@Query("id")String id,@Query("type")int type);
    @POST("addFriend.m")
    Observable<Result> addFriend(@Query("idA")String id1,@Query("idB")String id2);

}
