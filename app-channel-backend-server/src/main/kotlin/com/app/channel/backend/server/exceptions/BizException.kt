package com.app.channel.backend.server.exceptions

/**
 * @author liuzhongao
 * @since 2024/6/29 02:40
 */
class BizException(val code: Int, message: String) : Throwable(message)