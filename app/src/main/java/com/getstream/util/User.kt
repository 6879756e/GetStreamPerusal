package com.getstream.util

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User


fun User.getJobDetail(): String = extraData[job]?.toString() ?: ""

fun User.setJobDetail(jobDetail: String) {
    extraData[job] = jobDetail
}

fun User.getStatus(): String = extraData[statusKey]?.toString() ?: ""

fun User.setStatus(status: String) {
    extraData[statusKey] = status
}

fun User.isSelf(): Boolean = this.id == ChatClient.instance().getCurrentUser()?.id

fun User.appearsOnline(): Boolean = !invisible && online


val User.statusKey
    get() = "status"

val User.job
    get() = "job"