package com.application.channel.message.controller

import com.application.channel.message.Context
import com.application.channel.message.meta.Message
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/1 21:32
 */
internal class MessageDispatcherController @Inject constructor() : Controller<Message> {
    override fun handle(ctx: Context, message: Message) {
        TODO("Not yet implemented")
    }
}