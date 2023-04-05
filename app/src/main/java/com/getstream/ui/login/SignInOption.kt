package com.getstream.ui.login

import androidx.annotation.DrawableRes
import com.getstream.R

enum class SignInOption(@DrawableRes val resId: Int, val type: String) {
    GOOGLE(resId = R.drawable.google_icon, type = "Google"),
    APPLE(resId = R.drawable.apple_icon, type = "Apple"),
    EMAIL(resId = R.drawable.email_icon, type = "email"),
}