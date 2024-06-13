package com.application.channel.database.meta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.channel.database.LocalSessionTable

/**
 * @author liuzhongao
 * @since 2024/6/8 20:01
 */
@Entity(tableName = LocalSessionTable.TABLE_NAME)
data class LocalSessionContact(
    @PrimaryKey
    @ColumnInfo(name = LocalSessionTable.SESSION_ID, index = true) val sessionId: String,
    @ColumnInfo(name = LocalSessionTable.SESSION_TYPE_CODE, index = true) val sessionTypeCode: Int,
    @ColumnInfo(name = LocalSessionTable.SESSION_LAST_MODIFY_TIME, defaultValue = "0") val lastModifyTimestamp: Long = 0,
    @ColumnInfo(name = LocalSessionTable.SESSION_EXTENSIONS, defaultValue = "null") val extensions: String? = null
)