package com.getstream

import com.getstream.Env.EMAIL_EXISTS_BASE_URL
import com.getstream.Env.SIGN_UP_BASE_URL
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import timber.log.Timber

/**
 * TODO: Code on this file requires reorganising. The code includes the business logic for checking
 * whether an email address already exists (aws dynamoDB table is used).
 */

private val signUpRetrofit by lazy {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    Retrofit.Builder()
        .baseUrl(SIGN_UP_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

}

val signUpEmailApi by lazy {
    signUpRetrofit.create(SignUpEmailApi::class.java)
}


interface SignUpEmailApi {

    @POST("sign_up_email_address")
    suspend fun signUpEmailAddress(
        @Body emailSignUpRequest: Any
    ): Response<SignUpEmailResponse>

}

@JvmInline
value class EmailSignUpRequest(@SerializedName("email_address") val emailAddress: String)

data class SignUpEmailResponse(
    val statusCode: Int,
    val jwtToken: String,
)