package com.sibby.sibby.data.repository

import com.sibby.sibby.data.network.BaseRepository
import com.sibby.sibby.data.network.MyApi
import com.sibby.sibby.data.store.UserStore
import okhttp3.MultipartBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : BaseRepository() {

    suspend fun login(
        email: String,
        password: String,
        fcm: String,
    ) = safeApiCall {
        api.login(email, password, fcm)
    }

    suspend fun forgot(
        email: String
    ) = safeApiCall {
        api.forgot(email)
    }

    suspend fun user(
        token: String,
        name: String,
        uname: String,
        email: String,
        rs: String,
        address: String,
        hp: String
    ) = safeApiCall {
        api.user(token, name, uname, email, rs, address, hp)
    }

    suspend fun profile(
        token: String,
        profile: MultipartBody.Part
    ) = safeApiCall {
        api.profile(token, profile)
    }

    suspend fun logout(
        token: String
    ) = safeApiCall {
        api.logout(token)
    }

    suspend fun home(
        token: String
    ) = safeApiCall {
        api.home(token)
    }

    suspend fun saveToken(
        token: String
    ) = userStore.saveToken(
        token
    )

    fun user() = userStore.authToken

}