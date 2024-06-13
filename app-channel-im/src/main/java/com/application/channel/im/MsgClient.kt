package com.application.channel.im

/**
 * @author liuzhongao
 * @since 2024/6/10 09:53
 */
object MsgClient {

    private val msgServiceMap = HashMap<Class<*>, Any>()

    init {
        this.msgServiceMap[MsgConnectionService::class.java] = MsgConnectionService()
        this.msgServiceMap[MsgService::class.java] = MsgService()
    }

    fun <T> getService(clazz: Class<T>): T {
        return requireNotNull(this.msgServiceMap[clazz] as T)
    }
}