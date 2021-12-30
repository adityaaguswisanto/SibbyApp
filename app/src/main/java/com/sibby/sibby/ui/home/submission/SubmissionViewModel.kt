package com.sibby.sibby.ui.home.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibby.sibby.data.network.Resource
import com.sibby.sibby.data.repository.BabiesRepository
import com.sibby.sibby.data.responses.message.SubmissionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SubmissionViewModel @Inject constructor(
    private val repository: BabiesRepository
) : ViewModel() {

    private val _submissionResponse: MutableLiveData<Resource<SubmissionResponse>> =
        MutableLiveData()

    val submissionResponse: LiveData<Resource<SubmissionResponse>>
        get() = _submissionResponse

    fun store(
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
    ) = viewModelScope.launch {
        _submissionResponse.value = Resource.Loading
        _submissionResponse.value = repository.store(
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
        _submissionResponse.value = Resource.Dismiss
    }

    fun update(
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
    ) = viewModelScope.launch {
        _submissionResponse.value = Resource.Loading
        _submissionResponse.value = repository.update(
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
        _submissionResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}