package com.sibby.sibby.ui.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibby.sibby.data.network.Resource
import com.sibby.sibby.data.repository.AuthRepository
import com.sibby.sibby.data.responses.home.HomeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _homeResponse: MutableLiveData<Resource<HomeResponse>> = MutableLiveData()

    val homeResponse: LiveData<Resource<HomeResponse>>
        get() = _homeResponse

    fun home(token: String) = viewModelScope.launch {
        _homeResponse.value = Resource.Loading
        _homeResponse.value = repository.home(token)
        _homeResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}