package com.getstream.ui.login

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException

fun ComponentActivity.signInWithGoogle(
    callback: (GoogleSignInAccount?) -> Unit
): ActivityResultLauncher<GoogleSignInClient> = registerForActivityResult(
    object : ActivityResultContract<GoogleSignInClient, GoogleSignInAccount?>() {
        override fun createIntent(context: Context, input: GoogleSignInClient): Intent =
            input.signInIntent

        override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)

            return try {
                return task.getResult(ApiException::class.java)
            } catch (e: ApiException) {
                null
            }
        }
    }, callback
)