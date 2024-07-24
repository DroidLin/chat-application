package com.application.channel.database

interface LocalMessageTable {
    companion object {
        const val TABLE_NAME = "table_message_record"

        const val MESSAGE_ID = "message_id"
        const val MESSAGE_UUID = "message_uuid"
        const val MESSAGE_SESSION_TYPE_CODE = "message_session_type_code"
        const val MESSAGE_STATE_USER_CONSUMED = "message_state_user_consumed"
        const val MESSAGE_SENDER_SESSION_ID = "message_sender_session_id"
        const val MESSAGE_RECEIVER_SESSION_ID = "message_receiver_session_id"
        const val MESSAGE_TIMESTAMP = "message_timestamp"
        const val MESSAGE_CONTENT = "message_content"
        const val MESSAGE_EXTENSIONS = "message_extensions"
    }
}

interface LocalRecentContactTable {
    companion object {
        const val TABLE_NAME = "table_recent_contact"

        const val RECENT_SESSION_ID = "recent_session_id"
        const val RECENT_SESSION_TYPE_CODE = "recent_session_type_code"
        const val RECENT_TIMESTAMP = "recent_timestamp"
        const val RECENT_EXTENSIONS = "extensions"
    }
}

interface LocalSessionContactTable {
    companion object {
        const val TABLE_NAME = "table_session_contact"

        const val SESSION_ID = "session_id"
        const val SESSION_TYPE_CODE = "session_type_code"
        const val SESSION_LAST_MODIFY_TIME = "session_last_modify_time"
        const val SESSION_EXTENSIONS = "session_extensions"
    }
}