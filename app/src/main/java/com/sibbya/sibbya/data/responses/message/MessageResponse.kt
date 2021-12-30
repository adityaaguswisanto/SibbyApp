package com.sibbya.sibbya.data.responses.message

import com.sibbya.sibbya.data.responses.fcm.Result

data class ForgotResponse(
    val message: String
)

data class RijectResponse(
    val canonical_ids: Int,
    val failure: Int,
    val multicast_id: Long,
    val results: List<Result>,
    val success: Int
)

data class PdfResponse(
    val canonical_ids: Int,
    val failure: Int,
    val multicast_id: Long,
    val results: List<Result>,
    val success: Int
)

data class LogoutResponse(
    val message: String
)

data class AuthResponse(
    val message: String
)