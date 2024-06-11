package com.application.channel.im

import com.application.channel.message.ChatService

/**
 * @author liuzhongao
 * @since 2024/6/10 09:53
 */
object MsgClient {

    private val msgServiceMap = HashMap<Class<*>, Any>()

    init {
        val connectionService = MsgConnectionService()
    }

    fun <T> getService(clazz: Class<T>): T {
        return requireNotNull(this.msgServiceMap[clazz] as T)
    }
}