package com.app.channel.backend.server.room.metadata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * @author liuzhongao
 * @since 2024/6/26 22:15
 */
@Entity(
    tableName = "local_user_info",
    foreignKeys = [
        ForeignKey(
            entity = LocalAccount::class,
            parentColumns = ["account_id"],
            childColumns = ["user_account"]
        )
    ]
)
data class LocalUserInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id", index = true) val userId: Long,
    @ColumnInfo(name = "user_account", index = true) val userAccount: String,
    @ColumnInfo(name = "user_name", index = true) val userName: String,
    @ColumnInfo(name = "user_email", defaultValue = "null") val userEmail: String? = null,
    @ColumnInfo(name = "user_phone", defaultValue = "null") val userPhone: String? = null,
)
