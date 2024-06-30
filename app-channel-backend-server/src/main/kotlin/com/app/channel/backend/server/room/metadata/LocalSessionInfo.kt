package com.app.channel.backend.server.room.metadata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    ]
)
data class LocalSessionInfo(
    @PrimaryKey
    @ColumnInfo(name = "session_id") val sessionId: String,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "account_id") val accountId: String,
    @ColumnInfo(name = "session_type") val sessionTypeCode: Int,
)
