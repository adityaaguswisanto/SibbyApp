package com.sibby.sibby.data.network

import com.sibby.sibby.data.responses.auth.LoginResponse
import com.sibby.sibby.data.responses.babies.BabiesResponse
import com.sibby.sibby.data.responses.home.HomeResponse
import com.sibby.sibby.data.responses.message.*
import com.sibby.sibby.data.responses.news.NewsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MyApi {

    @FormUrlEncoded
    @POST("api/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("fcm") fcm: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("api/auth/forgot")
    suspend fun forgot(
        @Field("email") email: String,
    ): ForgotResponse

    @FormUrlEncoded
    @POST("api/auth/user")
    suspend fun user(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("uname") uname: String,
        @Field("email") email: String,
        @Field("rs") rs: String,
        @Field("address") address: String,
        @Field("hp") hp: String,
    ): AuthResponse

    @Multipart
    @POST("api/auth/profile")
    suspend fun profile(
        @Header("Authorization") token: String,
        @Part profile: MultipartBody.Part,
    ): AuthResponse

    @GET("api/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ): LogoutResponse

    @Multipart
    @POST("api/babies/")
    suspend fun store(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("dad") dad: RequestBody,
        @Part("mom") mom: RequestBody,
        @Part skl: MultipartBody.Part,
        @Part ktpd: MultipartBody.Part,
        @Part ktpm: MultipartBody.Part,
        @Part kk: MultipartBody.Part,
        @Part bk: MultipartBody.Part,
    ): SubmissionResponse

    @GET("api/babies/")
    suspend fun index(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("q") q: String,
    ): BabiesResponse

    @Multipart
    @POST("api/babies/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("dad") dad: RequestBody,
        @Part("mom") mom: RequestBody,
        @Part skl: MultipartBody.Part,
        @Part ktpd: MultipartBody.Part,
        @Part ktpm: MultipartBody.Part,
        @Part kk: MultipartBody.Part,
        @Part bk: MultipartBody.Part,
    ): SubmissionResponse

    @GET("api/babies/status")
    suspend fun status(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("q") q: String,
    ): BabiesResponse

    @GET("api/news/")
    suspend fun news(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("q") q: String,
    ): NewsResponse

    @GET("api/auth/home")
    suspend fun home(
        @Header("Authorization") token: String,
    ): HomeResponse

}