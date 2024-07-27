package com.app.channel.backend.server.room.metadata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author liuzhongao
 * @since 2024/6/26 22:38
 */
@Entity(
    tableName = "local_account"
)
data class LocalAccount(
    @PrimaryKey
    @ColumnInfo(name = "account_id") val accountId: String,
    @ColumnInfo(name = "create_time") val createTime: Long,
    @ColumnInfo(name = "update_time") val updateTime: Long,
    @ColumnInfo(name = "password_hash") val passwordHash: String,
)