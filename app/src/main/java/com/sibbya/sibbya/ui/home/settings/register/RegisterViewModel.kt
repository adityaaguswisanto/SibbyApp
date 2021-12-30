package com.sibbya.sibbya.ui.home.settings.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.data.repository.AuthRepository
import com.sibbya.sibbya.data.responses.message.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _registerResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()

    val registerResponse: LiveData<Resource<AuthResponse>>
        get() = _registerResponse

    fun register(
        token: String,
        name: String,
        uname: String,
        email: String,
        password: String,
        rs: String,
        address: String,
        hp: String,
        role: String,
    ) = viewModelScope.launch {
        _registerResponse.value = Resource.Loading
        _registerResponse.value = repository.register(
            token, name, uname, email, password, rs, address, hp, role
        )
        _registerResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}