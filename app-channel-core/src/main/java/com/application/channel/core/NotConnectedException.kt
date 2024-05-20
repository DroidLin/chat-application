package com.application.channel.core

/**
 * @author liuzhongao
 * @since 2024/5/12 12:03
 */
class NotConnectedException : Throwable {

    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor() : super()
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    )

    companion object {
        private const val serialVersionUID: Long = -3764747953356161167L
    }
}