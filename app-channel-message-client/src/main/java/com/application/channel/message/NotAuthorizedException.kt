package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/6/2 10:40
 */
class NotAuthorizedException : Throwable {

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
        private const val serialVersionUID: Long = 2275989420711755723L
    }
}