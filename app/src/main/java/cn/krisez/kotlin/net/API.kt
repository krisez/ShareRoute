package cn.krisez.kotlin.net

import cn.krisez.network.bean.Result
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface API {

    @GET("getPos.m")
    fun getOtherPos(@Query("id") id: String): Observable<Result>

    @POST("postPos.m")
    fun postPos(@Query("id") id: String, @Query("lat") lat: String, @Query("lng") lon: String, @Query("speed") speed: String, @Query("direction") direction: String, @Query("address") address: String, @Query("time") time: String): Observable<Result>

    @POST("login.m")
    fun login(@Query("mobile") id: String, @Query("pw") pw: String): Observable<Result>

    @POST("register.m")
    fun register(@Query("mobile") id: String): Observable<Result>

    @POST("updatePw.m")
    fun updatePw(@Query("id") id: String, @Query("pw") pw: String): Observable<Result>

    @POST("updateUser.m")
    fun updateUser(@Query("id")id:String,@Query("value")value:String?, @Query("key")ket:String?):Observable<Result>

    @POST("uploadFile.m")
    @Multipart
    fun uploadFile(@Query("id") id: String, @Part file: MultipartBody.Part): Observable<Result>

    @GET("getTraceHistory.m")
    fun getTraceHistory(@Query("userId")id:String):Observable<Result>

    @GET("getTracePoints.m")
    fun getTracePoints(@Query("userId")id:String,@Query("start")start:String,@Query("end")end:String):Observable<Result>
}
