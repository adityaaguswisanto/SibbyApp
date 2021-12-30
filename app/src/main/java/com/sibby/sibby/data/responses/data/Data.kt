package com.sibby.sibby.data.responses.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val address: String,
    val created_at: String,
    val email: String,
    val hp: String,
    val id: Int,
    val name: String,
    val profile: String?,
    val role: String,
    val rs: String,
    val uname: String,
    val updated_at: String
) : Parcelable

@Parcelize
data class Babies(
    val admin: String,
    val bk: String,
    val created_at: String,
    val dad: String,
    val gender: String,
    val id_baby: Int,
    val kk: String,
    val ktpd: String,
    val ktpm: String,
    val mom: String,
    val pdf: String?,
    val name: String,
    val skl: String,
    val status: String,
    val updated_at: String,
    val user: User
) : Parcelable

@Parcelize
data class News(
    val admin: Int,
    val body: String,
    val created_at: String,
    val id_news: Int,
    val image: String,
    val title: String,
    val updated_at: String
) : Parcelable