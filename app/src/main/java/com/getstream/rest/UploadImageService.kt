package com.getstream.rest

import com.getstream.Env
import com.getstream.model.UploadImageBody
import com.getstream.model.UploadImageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface UploadImageApi {

    @POST("upload_profile_picture")
    suspend fun execute(
        @Body uploadImageBody: UploadImageBody
    ): Response<UploadImageResponse>
}


private val uploadImageRetrofit by lazy {
    buildDefaultRetrofitInstance(Env.UPLOAD_IMAGE_BASE_URL)
}


val uploadImageApi by lazy {
    uploadImageRetrofit.create(UploadImageApi::class.java)
}