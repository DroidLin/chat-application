package com.chat.compose.app.metadata

import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * @author liuzhongao
 * @since 2024/6/29 18:09
 */
@JsonClass(generateAdapter = true)
data class UserInfo(
    val userId: Long = -1,
    val userName: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 2302133063994557737L
    }
}

val UserInfo.isValid: Boolean
    get() = this.userId > 0