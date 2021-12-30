package com.sibby.sibby.ui.home.home.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sibby.sibby.data.network.MyApi
import com.sibby.sibby.data.responses.data.Babies
import com.sibby.sibby.data.source.StatusPaging
import com.sibby.sibby.data.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : ViewModel() {

    fun status(token: String, q: String): Flow<PagingData<Babies>> {
        return Pager(PagingConfig(50)) {
            StatusPaging(api, token, q)
        }.flow.cachedIn(viewModelScope)
    }

    fun user() = userStore.authToken

}