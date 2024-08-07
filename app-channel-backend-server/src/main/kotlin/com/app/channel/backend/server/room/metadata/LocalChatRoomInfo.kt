package com.app.channel.backend.server.room.metadata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * @author liuzhongao
 * @since 2024/8/2 16:51
 */
@Entity(
    tableName = "local_chat_room",
    foreignKeys = [
        ForeignKey(
            entity = LocalSessionInfo::class,
            parentColumns = ["session_id", "session_type"],
            childColumns = ["chat_room_session_id", "chat_room_session_type"]
        ),
        ForeignKey(
            entity = LocalSessionInfo::class,
            parentColumns = ["session_id", "session_type"],
            childColumns = ["chat_room_creator_session_id", "chat_room_creator_session_type"]
        ),
        ForeignKey(
            entity = LocalAccount::class,
            parentColumns = ["account_id"],
            childColumns = ["chat_room_account_id"]
        )
    ]
)
data class LocalChatRoomInfo(
    @PrimaryKey
    @ColumnInfo(name = "chat_room_session_id") val sessionId: String,
    @ColumnInfo(name = "chat_room_session_type") val sessionTypeCode: Int,
    @ColumnInfo(name = "chat_room_account_id") val accountId: String,
    @ColumnInfo(name = "chat_room_create_time") val createTimeStamp: Long,
    @ColumnInfo(name = "chat_room_update_time") val updateTimeStamp: Long,
    @ColumnInfo(name = "chat_room_creator_session_id") val creatorSessionId: String,
    @ColumnInfo(name = "chat_room_creator_session_type") val creatorSessionTypeCode: Int,
    @ColumnInfo(name = "chat_room_members", defaultValue = "\'\'") val members: String = "",
    @ColumnInfo(name = "chat_room_members_manager", defaultValue = "\'\'") val memberManager: String = ""
)

