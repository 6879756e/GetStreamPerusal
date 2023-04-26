package com.getstream.features.chat

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    val channelId = MutableStateFlow<String?>(null)

    fun setChannelId(cid: String?) {
        channelId.value = cid
    }

}