package com.chat.compose.app.metadata

import com.android.dependencies.common.INoProguard
import com.squareup.moshi.JsonClass

/**
 * @author liuzhongao
 * @since 2024/7/4 23:57
 */
@JsonClass(generateAdapter = true)
data class SearchComplexResource(
    val userInfo: List<Profile>
): INoProguard {

    val isEmpty: Boolean
        get() = this.userInfo.isEmpty()
}