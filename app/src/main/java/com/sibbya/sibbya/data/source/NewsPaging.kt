package com.sibbya.sibbya.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sibbya.sibbya.data.network.MyApi
import com.sibbya.sibbya.data.responses.data.News
import javax.inject.Inject

class NewsPaging @Inject constructor(
    private val api: MyApi,
    private val token: String,
    private val q: String
) : PagingSource<Int, News>() {

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.news(token, nextPageNumber, q)
            val news = response.data
            LoadResult.Page(
                data = response.data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (news.isEmpty()) null else nextPageNumber + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}