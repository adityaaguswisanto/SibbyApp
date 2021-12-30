package com.sibbya.sibbya.ui.home.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.data.repository.AuthRepository
import com.sibbya.sibbya.data.responses.message.LogoutResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _logoutResponse: MutableLiveData<Resource<LogoutResponse>> = MutableLiveData()

    val logoutResponse: LiveData<Resource<LogoutResponse>>
        get() = _logoutResponse

    fun logout(token: String) = viewModelScope.launch {
        _logoutResponse.value = Resource.Loading
        _logoutResponse.value = repository.logout(token)
        _logoutResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}