package com.sibbya.sibbya.data.repository

import com.sibbya.sibbya.data.network.BaseRepository
import com.sibbya.sibbya.data.network.MyApi
import com.sibbya.sibbya.data.store.UserStore
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class BabiesRepository @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : BaseRepository() {

    suspend fun riject(
        token: String,
        id: Int,
        user: Int,
        baby: String,
        status: String
    ) = safeApiCall {
        api.riject(
            token,
            id,
            user,
            baby,
            status
        )
    }

    suspend fun pdf(
        token: String,
        id: Int,
        user: Int,
        baby: RequestBody,
        pdf: MultipartBody.Part,
        status: RequestBody
    ) = safeApiCall {
        api.pdf(
            token,
            id,
            user,
            baby,
            pdf,
            status
        )
    }

    fun user() = userStore.authToken

}