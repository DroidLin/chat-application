package com.app.channel.backend.server

import com.app.channel.backend.server.metadata.ApiResult
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

/**
 * @author liuzhongao
 * @since 2024/6/26 01:26
 */

private val BizAfterCall = PipelinePhase("BizFallback")
private val API_RESULT_NOT_FOUND = ApiResult.failure(code = 404, message = "接口未找到！")

fun Application.interceptor() {
    interceptorPhases()
    bizInterceptor()
}

private fun Application.interceptorPhases() {
    insertPhaseAfter(ApplicationCallPipeline.ApplicationPhase.Call, BizAfterCall)
}

private fun Application.bizInterceptor() {
    intercept(BizAfterCall) {
        if (call.isHandled) return@intercept finish()
        call.respond(API_RESULT_NOT_FOUND)
        finish()
    }
}