package com.sibbya.sibbya.ui.home.babies.submission.draft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.data.repository.BabiesRepository
import com.sibbya.sibbya.data.responses.message.PdfResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class DraftViewModel @Inject constructor(
    private val repository: BabiesRepository
) : ViewModel() {

    private val _pdfResponse: MutableLiveData<Resource<PdfResponse>> =
        MutableLiveData()

    val pdfResponse: LiveData<Resource<PdfResponse>>
        get() = _pdfResponse

    fun pdf(
        token: String,
        id: Int,
        user: Int,
        baby: RequestBody,
        pdf: MultipartBody.Part,
        status: RequestBody
    ) = viewModelScope.launch {
        _pdfResponse.value = Resource.Loading
        _pdfResponse.value = repository.pdf(token, id, user, baby, pdf, status)
        _pdfResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}