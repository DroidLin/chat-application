package com.chat.compose.app.metadata

import com.application.channel.message.SessionType
import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * @author liuzhongao
 * @since 2024/6/30 01:33
 */
@JsonClass(generateAdapter = true)
data class SessionInfo(
    val sessionId: String? = null,
    val accountId: String? = null,
    val userId: Long = 0,
    val sessionTypeCode: Int = 0,
) : Serializable {

    val sessionType: SessionType = SessionType.fromValue(this.sessionTypeCode)

    companion object {
        private const val serialVersionUID: Long = 9022488129018406015L
    }
}

val SessionInfo.isValid: Boolean
    get() = !this.sessionId .isNullOrBlank() && this.sessionId.isNotEmpty()
            && !this.accountId.isNullOrBlank() && this.accountId.isNotEmpty()
            && this.userId > 0