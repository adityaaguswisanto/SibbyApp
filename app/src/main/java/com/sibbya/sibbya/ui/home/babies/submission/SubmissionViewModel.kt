package com.sibbya.sibbya.ui.home.babies.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.data.repository.BabiesRepository
import com.sibbya.sibbya.data.responses.message.RijectResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SubmissionViewModel @Inject constructor(
    private val repository: BabiesRepository
) : ViewModel() {

    private val _submissionResponse: MutableLiveData<Resource<RijectResponse>> =
        MutableLiveData()

    val submissionResponse: LiveData<Resource<RijectResponse>>
        get() = _submissionResponse

    fun riject(
        token: String,
        id: Int,
        user: Int,
        baby: String,
        status: String
    ) = viewModelScope.launch {
        _submissionResponse.value = Resource.Loading
        _submissionResponse.value = repository.riject(token, id, user, baby, status)
        _submissionResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}