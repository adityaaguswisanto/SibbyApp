package com.sibbya.sibbya.data.network

import com.sibbya.sibbya.data.responses.auth.LoginResponse
import com.sibbya.sibbya.data.responses.babies.BabiesResponse
import com.sibbya.sibbya.data.responses.home.HomeResponse
import com.sibbya.sibbya.data.responses.message.*
import com.sibbya.sibbya.data.responses.news.NewsResponse
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

    @GET("api/auth/home")
    suspend fun home(
        @Header("Authorization") token: String,
    ): HomeResponse

    @GET("api/news/")
    suspend fun news(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("q") q: String,
    ): NewsResponse

    @GET("api/babies/admin")
    suspend fun index(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("q") q: String,
    ): BabiesResponse

    @FormUrlEncoded
    @POST("api/babies/riject/{id}")
    suspend fun riject(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("user") user: Int,
        @Field("baby") baby: String,
        @Field("status") status: String,
    ): RijectResponse

    @GET("api/babies/babies")
    suspend fun status(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("q") q: String,
    ): BabiesResponse

    @Multipart
    @POST("api/babies/pdf/{id}")
    suspend fun pdf(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("user") user: Int,
        @Part("baby") baby: RequestBody,
        @Part pdf: MultipartBody.Part,
        @Part("status") status: RequestBody,
    ): PdfResponse

    @GET("api/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ): LogoutResponse

    @FormUrlEncoded
    @POST("api/auth/admin")
    suspend fun user(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("uname") uname: String,
        @Field("email") email: String,
        @Field("address") address: String,
        @Field("hp") hp: String,
    ): AuthResponse

    @Multipart
    @POST("api/auth/profile")
    suspend fun profile(
        @Header("Authorization") token: String,
        @Part profile: MultipartBody.Part,
    ): AuthResponse

    @FormUrlEncoded
    @POST("api/auth/register")
    suspend fun register(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("uname") uname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("rs") rs: String,
        @Field("address") address: String,
        @Field("hp") hp: String,
        @Field("role") role: String,
    ): AuthResponse

}