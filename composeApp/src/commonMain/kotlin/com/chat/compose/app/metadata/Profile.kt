package com.chat.compose.app.metadata

import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * @author liuzhongao
 * @since 2024/6/30 01:33
 */
@JsonClass(generateAdapter = true)
data class Profile(
    val userInfo: UserInfo? = null,
    val account: Account? = null,
    val sessionInfo: SessionInfo? = null
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 8345646150213276042L
    }
}

val Profile.isValid: Boolean
    get() = this.userInfo?.isValid == true && this.account?.isValid == true && this.sessionInfo?.isValid == true