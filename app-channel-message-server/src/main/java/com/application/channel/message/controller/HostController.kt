package com.application.channel.message.controller

import com.application.channel.message.Context
import com.application.channel.message.meta.LoginMessage
import com.application.channel.message.meta.Message
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/5/31 22:16
 */
internal class HostController @Inject constructor(
    private val loginController: LoginController,
    private val dispatcherController: MessageDispatcherController
) : Controller<Message> {

    override fun handle(ctx: Context, message: Message) {
        if (message is LoginMessage) {
            this.loginController.handle(ctx, message)
        } else this.dispatcherController.handle(ctx, message)
    }
}