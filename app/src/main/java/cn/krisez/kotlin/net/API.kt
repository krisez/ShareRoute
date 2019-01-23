package cn.krisez.kotlin.net

import cn.krisez.network.bean.Result
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    @GET("getPos.m")
    fun getOtherPos(@Query("id") id: String): Observable<Result>

    @POST("postPos.m")
    fun postPos(@Query("id") id: String, @Query("lat") lat: String, @Query("lon") lon: String): Observable<Result>

    @POST("login.m")
    fun login(@Query("id") id: String,@Query("pw")pw:String): Observable<Result>
}
