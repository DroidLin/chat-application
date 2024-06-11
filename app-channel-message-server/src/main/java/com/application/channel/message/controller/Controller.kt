package com.application.channel.message.controller

import com.application.channel.message.Context
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/5/31 22:11
 */
interface Controller<T : Message> {

    fun handle(ctx: Context, message: T)
}