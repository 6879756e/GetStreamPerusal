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

private val emailCheckRetrofit by lazy {
    buildRetrofitInstance(EMAIL_EXISTS_BASE_URL)
}

val emailCheckApi by lazy {
    emailCheckRetrofit.create(EmailCheckAPI::class.java)
}

private val signUpRetrofit by lazy {
    buildRetrofitInstance(SIGN_UP_BASE_URL)
}

val signUpEmailApi by lazy {
    signUpRetrofit.create(SignUpEmailApi::class.java)
}

private fun buildRetrofitInstance(baseUrl: String): Retrofit {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}


interface EmailCheckAPI {

    @POST("email_address_exists")
    suspend fun checkEmailAddressExists(
        @Body emailCheckRequest: EmailCheckRequest
    ): Response<EmailAddressExistsResponse>

}

@JvmInline
value class EmailCheckRequest(@SerializedName("email_address") val emailAddress: String)

data class EmailAddressExistsResponse(
    val statusCode: Int,
    val exists: Boolean,
)

interface SignUpEmailApi {

    @POST("sign_up_email_address")
    suspend fun signUpEmailAddress(
        @Body emailSignUpRequest: EmailSignUpRequest
    ): Response<SignUpEmailResponse>

}

@JvmInline
value class EmailSignUpRequest(@SerializedName("email_address") val emailAddress: String)

data class SignUpEmailResponse(
    val statusCode: Int,
    val requestCode: Int,
)

//MainScope().launch {
//    withContext(this.coroutineContext) {
//        val emailAddress = "test@gmail.com"
//        val test = EmailCheckRequest(emailAddress)
//        emailCheckApi.checkEmailAddressExists(test)
//            .runCatching {
//                if (this.isSuccessful) {
//                    if (this.body()?.exists == true) {
//                        Timber.e("Email address already exists!")
//                    } else {
//                        signUp(emailAddress)
//                    }
//                }
//            }
//    }
//}

private suspend fun signUp(emailAddress: String) =
    signUpEmailApi.signUpEmailAddress(EmailSignUpRequest(emailAddress))
        .runCatching {
            this.body()?.run {
                if (requestCode == -1) {
                    Timber.e("Email Already Exists!")
                } else {
                    Timber.e("Email successfully added to database")
                }
            } ?: Timber.e("Network error?!")
        }