package com.application.channel.im

import com.application.channel.message.metadata.MutableSessionContact
import com.application.channel.message.metadata.SessionContact

/**
 * @author liuzhongao
 * @since 2024/6/12 00:57
 */

interface SessionExtensions {
    companion object {
        const val KEY_DRAFT_MESSAGE_STRING = "draft_message_string"
        const val KEY_USER_INFO_NAME = "user_info_name"
        const val KEY_USER_INFO_ID = "user_info_id"
        const val KEY_USER_INFO_STRING = "user_info_string"
    }
}

var MutableSessionContact.draftMessage: String?
    set(value) { this.extensions[SessionExtensions.KEY_DRAFT_MESSAGE_STRING] = value }
    get() = (this as SessionContact).draftMessage

var MutableSessionContact.userName: String?
    set(value) { this.extensions[SessionExtensions.KEY_USER_INFO_NAME] = value }
    get() = (this as SessionContact).userName

var MutableSessionContact.userId: Long?
    set(value) { this.extensions[SessionExtensions.KEY_USER_INFO_ID] = value }
    get() = (this as SessionContact).userId

var MutableSessionContact.userInfoString: String
    set(value) { this.extensions[SessionExtensions.KEY_USER_INFO_STRING] = value }
    get() = (this as SessionContact).userInfoString

val SessionContact.draftMessage: String?
    get() = this.extensions[SessionExtensions.KEY_DRAFT_MESSAGE_STRING] as? String

val SessionContact.userName: String?
    get() = this.extensions[SessionExtensions.KEY_USER_INFO_NAME] as? String

val SessionContact.userId: Long
    get() = this.extensions[SessionExtensions.KEY_USER_INFO_ID]?.toString()?.toLongOrNull() ?: -1

val SessionContact.userInfoString: String
    get() = this.extensions[SessionExtensions.KEY_USER_INFO_STRING] as? String ?: ""