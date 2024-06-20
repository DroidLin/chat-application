package com.chat.compose.app.usecase

import com.application.channel.message.MessageRepository
import com.application.channel.message.metadata.SessionContact
import com.chat.compose.app.metadata.UiSessionContact
import com.chat.compose.app.metadata.toUiSessionContact
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FetchSessionListUseCaseImpl(private val messageRepository: MessageRepository) : FetchSessionListUseCase {

    init {
        GlobalScope.launch {
            this@FetchSessionListUseCaseImpl.messageRepository.insertSessionContact(sessionContactV1)
            this@FetchSessionListUseCaseImpl.messageRepository.insertSessionContact(sessionContactV2)
        }
    }

    override val sessionList: Flow<List<UiSessionContact>> =
        this.messageRepository.fetchObservableSessionContactList(Int.MAX_VALUE)
            .map { sessionContactList -> sessionContactList.map(SessionContact::toUiSessionContact) }
            .distinctUntilChanged()
}