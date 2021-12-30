package com.sibby.sibby.ui.home.home.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibby.sibby.data.network.Resource
import com.sibby.sibby.data.repository.AuthRepository
import com.sibby.sibby.data.responses.message.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _userResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()

    val userResponse: LiveData<Resource<AuthResponse>>
        get() = _userResponse

    fun user(
        token: String,
        name: String,
        uname: String,
        email: String,
        rs: String,
        address: String,
        hp: String
    ) = viewModelScope.launch {
        _userResponse.value = Resource.Loading
        _userResponse.value = repository.user(token, name, uname, email, rs, address, hp)
        _userResponse.value = Resource.Dismiss
    }

    fun profile(
        token: String,
        profile: MultipartBody.Part,
    ) = viewModelScope.launch {
        _userResponse.value = Resource.Loading
        _userResponse.value = repository.profile(token, profile)
        _userResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}