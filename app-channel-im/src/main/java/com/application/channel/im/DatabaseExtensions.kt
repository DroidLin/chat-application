package com.application.channel.im

import com.application.channel.message.metadata.SessionContact

/**
 * @author liuzhongao
 * @since 2024/6/12 00:57
 */

interface SessionExtensions {
    companion object {
        const val KEY_DRAFT_MESSAGE_STRING = "draft_message_string"
    }
}

val SessionContact.draftMessage: String?
    get() = this.extensions[SessionExtensions.KEY_DRAFT_MESSAGE_STRING] as? String