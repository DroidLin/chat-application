package com.app.channel.backend.server.component

import com.application.channel.message.ChatService
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @author liuzhongao
 * @since 2024/6/30 23:41
 */
@Component
@Order
class ChatChannelCommandLineRunner : CommandLineRunner {
    override fun run(vararg args: String?) {
        val chatService = ChatService()
        chatService.startService()
    }
}