package com.application.channel.database.meta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.application.channel.database.LocalSessionContactTable

/**
 * @author liuzhongao
 * @since 2024/6/8 20:01
 */
@Entity(
    tableName = LocalSessionContactTable.TABLE_NAME,
    indices = [
        Index(value = [LocalSessionContactTable.SESSION_ID], unique = true),
        Index(value = [LocalSessionContactTable.SESSION_TYPE_CODE], unique = true)
    ]
)
data class LocalSessionContact(
    @PrimaryKey
    @ColumnInfo(name = LocalSessionContactTable.SESSION_ID) val sessionId: String,
    @ColumnInfo(name = LocalSessionContactTable.SESSION_TYPE_CODE) val sessionTypeCode: Int,
    @ColumnInfo(name = LocalSessionContactTable.SESSION_LAST_MODIFY_TIME, defaultValue = "0") val lastModifyTimestamp: Long = 0,
    @ColumnInfo(name = LocalSessionContactTable.SESSION_EXTENSIONS, defaultValue = "null") val extensions: String? = null
)