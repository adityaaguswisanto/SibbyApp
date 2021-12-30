package com.sibby.sibby.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibby.sibby.data.network.Resource
import com.sibby.sibby.data.repository.AuthRepository
import com.sibby.sibby.data.responses.auth.LoginResponse
import com.sibby.sibby.data.responses.message.ForgotResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    private val _forgotResponse: MutableLiveData<Resource<ForgotResponse>> = MutableLiveData()

    val forgotResponse: LiveData<Resource<ForgotResponse>>
        get() = _forgotResponse

    fun login(email: String, password: String, fcm: String) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password, fcm)
        _loginResponse.value = Resource.Dismiss
    }

    fun forgot(email: String) = viewModelScope.launch {
        _forgotResponse.value = Resource.Loading
        _forgotResponse.value = repository.forgot(email)
        _forgotResponse.value = Resource.Dismiss
    }

    suspend fun saveToken(token: String) = repository.saveToken(token)

}