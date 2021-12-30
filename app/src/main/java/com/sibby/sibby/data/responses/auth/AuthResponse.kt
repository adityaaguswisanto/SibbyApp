package com.sibby.sibby.data.responses.auth

import com.sibby.sibby.data.responses.data.User

data class LoginResponse(
    val token: String,
    val user: User
)