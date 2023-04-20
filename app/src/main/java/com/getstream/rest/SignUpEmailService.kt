package com.getstream.rest

import com.getstream.Env
import com.getstream.model.SignUpEmailBody
import com.getstream.model.SignUpEmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

private val signUpRetrofit by lazy {
    buildDefaultRetrofitInstance(Env.SIGN_UP_BASE_URL)
}


val signUpEmailApi by lazy {
    signUpRetrofit.create(SignUpEmailApi::class.java)
}


interface SignUpEmailApi {

    @POST("sign_up_email_address")
    suspend fun execute(
        @Body signUpEmailBody: SignUpEmailBody
    ): Response<SignUpEmailResponse>

}