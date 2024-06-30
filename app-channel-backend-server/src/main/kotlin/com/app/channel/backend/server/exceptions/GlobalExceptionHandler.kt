package com.app.channel.backend.server.exceptions

import com.app.channel.backend.server.metadata.ApiResult
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * @author liuzhongao
 * @since 2024/6/29 02:41
 */
@ControllerAdvice
class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = [BizException::class])
    fun bizExceptionHandler(bizException: BizException) = run {
        return@run ApiResult.failure(bizException.code, bizException.message)
    }
}