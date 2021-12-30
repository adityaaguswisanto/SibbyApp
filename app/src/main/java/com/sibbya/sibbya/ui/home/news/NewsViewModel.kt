package com.sibbya.sibbya.ui.home.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sibbya.sibbya.data.network.MyApi
import com.sibbya.sibbya.data.responses.data.News
import com.sibbya.sibbya.data.source.NewsPaging
import com.sibbya.sibbya.data.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : ViewModel() {

    fun index(token: String, q: String): Flow<PagingData<News>> {
        return Pager(PagingConfig(50)) {
            NewsPaging(api, token, q)
        }.flow.cachedIn(viewModelScope)
    }

    fun user() = userStore.authToken

}