package com.getstream.model

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(
    val statusCode: Int,
    @SerializedName("img_url") val imgUrl: String,
)