package com.app.channel.backend.server.room.metadata

import androidx.room.*

/**
 * @author liuzhongao
 * @since 2024/6/26 22:34
 */
@Entity(
    tableName = "local_session_info",
    foreignKeys = [
        ForeignKey(
            entity = LocalUserInfo::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
        ),
        ForeignKey(
            entity = LocalAccount::class,
            parentColumns = ["account_id"],
            childColumns = ["account_id"],
        )
    ],
    indices = [
        Index(value = ["session_id", "session_type"], unique = true),
    ]
)
data class LocalSessionInfo(
    @PrimaryKey
    @ColumnInfo(name = "session_id") val sessionId: String,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "account_id") val accountId: String,
    @ColumnInfo(name = "session_type") val sessionTypeCode: Int,
)
