package com.sibbya.sibbya.data.repository

import com.sibbya.sibbya.data.network.BaseRepository
import com.sibbya.sibbya.data.network.MyApi
import com.sibbya.sibbya.data.store.UserStore
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

    suspend fun logout(
        token: String
    ) = safeApiCall {
        api.logout(token)
    }

    suspend fun user(
        token: String,
        name: String,
        uname: String,
        email: String,
        address: String,
        hp: String
    ) = safeApiCall {
        api.user(token, name, uname, email, address, hp)
    }

    suspend fun profile(
        token: String,
        profile: MultipartBody.Part
    ) = safeApiCall {
        api.profile(token, profile)
    }

    suspend fun home(
        token: String
    ) = safeApiCall {
        api.home(token)
    }

    suspend fun register(
        token: String,
        name: String,
        uname: String,
        email: String,
        password: String,
        rs: String,
        address: String,
        hp: String,
        role: String,
    ) = safeApiCall {
        api.register(
            token, name, uname, email, password, rs, address, hp, role
        )
    }

    suspend fun saveToken(
        token: String
    ) = userStore.saveToken(
        token
    )

    fun user() = userStore.authToken

}