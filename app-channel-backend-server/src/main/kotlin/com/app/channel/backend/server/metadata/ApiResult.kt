package com.app.channel.backend.server.metadata

/**
 * @author liuzhongao
 * @since 2024/6/26 00:17
 */
data class ApiResult<T>(
    val data: T? = null,
    val code: Int = 200,
    val message: String? = null,
) {

    companion object {
        fun <T> success(data: T?) = ApiResult<T>(data = data)
        fun failure(code: Int, message: String? = null) = ApiResult<Any>(code = code, message = message)
    }
}
