package com.chat.compose.app.usecase

import com.application.channel.im.MsgConnectionService
import com.application.channel.im.session.ChatSession

/**
 * @author liuzhongao
 * @since 2024/6/20 01:09
 */
class CloseSessionUseCase(private val msgConnectionService: MsgConnectionService) {

    fun closeSession(chatSession: ChatSession) {
        this.msgConnectionService.closeSession(chatSession)
    }
}