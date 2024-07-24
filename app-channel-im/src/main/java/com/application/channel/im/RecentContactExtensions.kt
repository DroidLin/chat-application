package com.application.channel.im

import com.application.channel.message.metadata.MutableRecentContact
import com.application.channel.message.metadata.RecentContact

/**
 * @author liuzhongao
 * @since 2024/7/25 00:31
 */

var MutableRecentContact.draftMessage: String?
    set(value) { this.extensions[SessionExtensions.KEY_DRAFT_MESSAGE_STRING] = value }
    get() = (this as RecentContact).draftMessage

var MutableRecentContact.userName: String?
    set(value) { this.extensions[SessionExtensions.KEY_USER_INFO_NAME] = value }
    get() = (this as RecentContact).userName

var MutableRecentContact.userId: Long?
    set(value) { this.extensions[SessionExtensions.KEY_USER_INFO_ID] = value }
    get() = (this as RecentContact).userId

var MutableRecentContact.userInfoString: String
    set(value) { this.extensions[SessionExtensions.KEY_USER_INFO_STRING] = value }
    get() = (this as RecentContact).userInfoString

val RecentContact.draftMessage: String?
    get() = this.extensions[SessionExtensions.KEY_DRAFT_MESSAGE_STRING] as? String

val RecentContact.userName: String?
    get() = this.extensions[SessionExtensions.KEY_USER_INFO_NAME] as? String

val RecentContact.userId: Long
    get() = this.extensions[SessionExtensions.KEY_USER_INFO_ID]?.toString()?.toLongOrNull() ?: -1

val RecentContact.userInfoString: String
    get() = this.extensions[SessionExtensions.KEY_USER_INFO_STRING] as? String ?: ""