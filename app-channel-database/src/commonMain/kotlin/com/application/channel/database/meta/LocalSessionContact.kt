package com.application.channel.database.meta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.channel.database.MessageDatabase

/**
 * @author liuzhongao
 * @since 2024/6/8 20:01
 */
@Entity(tableName = MessageDatabase.LocalSessionTable.TABLE_NAME)
data class LocalSessionContact(
    @PrimaryKey
    @ColumnInfo(name = MessageDatabase.LocalSessionTable.SESSION_ID, index = true) val sessionId: String,
    @ColumnInfo(name = MessageDatabase.LocalSessionTable.SESSION_TYPE_CODE, index = true) val sessionTypeCode: Int,
    @ColumnInfo(name = MessageDatabase.LocalSessionTable.SESSION_LAST_MODIFY_TIME, defaultValue = "0") val lastModifyTimestamp: Long = 0,
    @ColumnInfo(name = MessageDatabase.LocalSessionTable.SESSION_EXTENSIONS, defaultValue = "null") val extensions: String? = null
)