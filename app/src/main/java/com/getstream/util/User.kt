package com.getstream.util

import io.getstream.chat.android.client.models.User


fun User.getJobDetail(): String = extraData["job"]?.toString() ?: ""

fun User.getStatus(): String = extraData["status"]?.toString() ?: ""