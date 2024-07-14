package com.chat.compose.app.network

import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/6/29 18:40
 */
const val DEFAULT_HOST = "192.168.2.110"
const val DEFAULT_PORT = 8080
const val TCP_PORT = 8081

const val DEFAULT_HOST_URL = "http://$DEFAULT_HOST:$DEFAULT_PORT"

suspend fun HttpClient.serverRequest(
    path: String,
    method: HttpMethod = HttpMethod.Post,
    parameters: Parameters
): ApiResult<Any> {
    return kotlin.runCatching {
        val fixPath = if (path.startsWith("/")) path else "/${path}"
        val httpResponse = this.submitForm(
            url = "$DEFAULT_HOST_URL$fixPath",
            formParameters = parameters,
            encodeInQuery = method == HttpMethod.Get
        ) {}

        val bodyTextResponse = httpResponse.bodyAsText()

        val jsonObject = kotlin.runCatching { JSONObject(bodyTextResponse) }
            .onFailure { it.printStackTrace() }
            .getOrNull()

        val code = jsonObject?.optInt("code", -1) ?: -1
        val data = jsonObject?.opt("data")
        val message = jsonObject?.optString("message")
        return ApiResult<Any>(code = code, data = data, message = message)
    }
        .onFailure { it.printStackTrace() }
        .getOrElse { throwable -> ApiResult(code = 400, message = throwable.message) }
}