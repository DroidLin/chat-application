package com.chat.compose.app.metadata

import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * @author liuzhongao
 * @since 2024/6/30 01:32
 */
@JsonClass(generateAdapter = true)
data class Account(
    val accountId: String? = null,
    val createTime: Long = 0,
    val updateTime: Long = 0,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 7053030318071355687L
    }
}

val Account.isValid: Boolean get() = !this.accountId.isNullOrBlank() && this.accountId.isNotEmpty()