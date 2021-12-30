package com.sibby.sibby.data.responses.home

import android.os.Parcelable
import com.sibby.sibby.data.responses.data.News
import com.sibby.sibby.data.responses.data.User
import kotlinx.parcelize.Parcelize

data class Submission(
    val name: String,
    val title: String,
    val desc: String,
    val imageId: Int
)

@Parcelize
data class HomeResponse(
    val user: User,
    val news: List<News>
) : Parcelable