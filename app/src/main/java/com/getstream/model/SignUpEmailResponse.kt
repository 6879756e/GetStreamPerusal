package com.getstream.model

data class SignUpEmailResponse(
    val statusCode: Int,
    val jwtToken: String,
)