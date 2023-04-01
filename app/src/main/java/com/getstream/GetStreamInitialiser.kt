package com.getstream

import android.app.Application
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory

fun Application.buildChatClient() {
    val offlinePluginFactory = StreamOfflinePluginFactory(
        config = Config(
            backgroundSyncEnabled = true,
            userPresence = true,
            persistenceEnabled = true,
            uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
            useSequentialEventHandler = false,
        ),
        appContext = this,
    )

    ChatClient.Builder(Env.API_KEY, this).withPlugin(offlinePluginFactory)
        .logLevel(ChatLogLevel.ALL).build()
}