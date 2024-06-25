package com.app.channel.backend.server

import com.app.channel.backend.server.metadata.ApiResult
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

/**
 * @author liuzhongao
 * @since 2024/6/26 01:26
 */

private val BizFallback = PipelinePhase("BizFallback")

fun Application.interceptor() {
    insertPhaseAfter(ApplicationCallPipeline.ApplicationPhase.Call, BizFallback)
    intercept(BizFallback) {
        if (call.isHandled) return@intercept finish()
        call.respond(ApiResult.failure(code = 404, message = "接口未找到！"))
        finish()
    }
}