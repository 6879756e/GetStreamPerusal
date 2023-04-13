package com.getstream.util

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User


fun User.getJobDetail(): String = extraData["job"]?.toString() ?: ""

fun User.getStatus(): String = extraData["status"]?.toString() ?: ""

fun User.isSelf(): Boolean = this.id == ChatClient.instance().getCurrentUser()?.id