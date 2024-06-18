package com.chat.compose.app.screen.message.vm

import com.application.channel.message.MessageRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/18 00:55
 */
class SessionListViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {

    val sessionList = this.messageRepository.fetchObservableSessionContactList(limit = Int.MAX_VALUE)
        .stateIn(this.viewModelScope, SharingStarted.Lazily, emptyList())
}
