package com.getstream.model

import com.google.gson.annotations.SerializedName

@JvmInline
value class SignUpEmailBody(@SerializedName("email_address") val emailAddress: String)