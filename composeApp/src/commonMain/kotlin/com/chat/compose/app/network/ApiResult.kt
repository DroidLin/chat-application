package com.chat.compose.app.network

import java.io.Serial
import java.io.Serializable

/**
 * @author liuzhongao
 * @since 2024/6/29 18:50
 */
data class ApiResult<T> internal constructor(
    val code: Int = 200,
    val data: T? = null,
    val message: String? = null
): Serializable {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -6933597811927185231L
    }
}

val ApiResult<*>.isSuccess: Boolean get() = this.code in 200..299