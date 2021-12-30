package com.sibbya.sibbya.data.responses.auth

import com.sibbya.sibbya.data.responses.data.User

data class LoginResponse(
    val token: String,
    val user: User
)

data class UserResponse(
    val user: User
)