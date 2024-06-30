package com.app.channel.backend.server.metadata

/**
 * @author liuzhongao
 * @since 2024/6/26 00:17
 */
data class ApiResult<T> internal constructor(
    val data: T? = null,
    val code: Int = 200,
    val message: String? = null,
) {

    companion object {
        fun <T> success(data: T?) = ApiResult(code = 200, data = data, message = null)
        fun failure(code: Int, message: String? = null) = ApiResult<Any>(code = code, message = message)
    }
}
