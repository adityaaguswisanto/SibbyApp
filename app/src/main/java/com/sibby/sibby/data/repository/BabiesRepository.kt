package com.sibby.sibby.data.repository

import com.sibby.sibby.data.network.BaseRepository
import com.sibby.sibby.data.network.MyApi
import com.sibby.sibby.data.store.UserStore
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class BabiesRepository @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : BaseRepository() {

    suspend fun store(
        token: String,
        name: RequestBody,
        gender: RequestBody,
        dad: RequestBody,
        mom: RequestBody,
        skl: MultipartBody.Part,
        ktpd: MultipartBody.Part,
        ktpm: MultipartBody.Part,
        kk: MultipartBody.Part,
        bk: MultipartBody.Part,
    ) = safeApiCall {
        api.store(
            token,
            name,
            gender,
            dad,
            mom,
            skl,
            ktpd,
            ktpm,
            kk,
            bk
        )
    }

    suspend fun update(
        token: String,
        id: Int,
        name: RequestBody,
        gender: RequestBody,
        dad: RequestBody,
        mom: RequestBody,
        skl: MultipartBody.Part,
        ktpd: MultipartBody.Part,
        ktpm: MultipartBody.Part,
        kk: MultipartBody.Part,
        bk: MultipartBody.Part,
    ) = safeApiCall {
        api.update(
            token,
            id,
            name,
            gender,
            dad,
            mom,
            skl,
            ktpd,
            ktpm,
            kk,
            bk
        )
    }

    fun user() = userStore.authToken

}