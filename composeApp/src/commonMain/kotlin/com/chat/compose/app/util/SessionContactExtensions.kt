package com.chat.compose.app.util

import com.application.channel.im.userInfoString
import com.application.channel.message.metadata.RecentContact
import com.chat.compose.app.metadata.Profile

/**
 * @author liuzhongao
 * @since 2024/7/13 11:39
 */
val RecentContact.profile: Profile?
    get() = kotlin.runCatching { fromJson<Profile>(this.userInfoString) }.getOrNull()