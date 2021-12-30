package com.sibby.sibby.data.responses.message

data class LogoutResponse(
    val message: String
)

data class ForgotResponse(
    val message: String
)

data class AuthResponse(
    val message: String
)

data class SubmissionResponse(
    val message_id: Long
)