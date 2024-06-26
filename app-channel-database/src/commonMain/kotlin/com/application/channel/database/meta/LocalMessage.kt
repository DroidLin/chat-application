package com.application.channel.database.meta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.channel.database.LocalMessageTable

/**
 * @author liuzhongao
 * @since 2024/6/8 18:07
 */
@Entity(
    tableName = LocalMessageTable.TABLE_NAME,
)
data class LocalMessage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = LocalMessageTable.MESSAGE_ID, index = true) val id: Int = 0,
    @ColumnInfo(name = LocalMessageTable.MESSAGE_UUID, index = true) val uuid: String,
    @ColumnInfo(name = LocalMessageTable.MESSAGE_SESSION_TYPE_CODE, index = true) val sessionTypeCode: Int,
    @ColumnInfo(name = LocalMessageTable.MESSAGE_STATE_USER_CONSUMED) val stateUserConsumed: Boolean = false,
    @ColumnInfo(name = LocalMessageTable.MESSAGE_SENDER_SESSION_ID, defaultValue = "\'\'") val senderSessionId: String = "",
    @ColumnInfo(name = LocalMessageTable.MESSAGE_RECEIVER_SESSION_ID, defaultValue = "\'\'") val receiverSessionId: String = "",
    @ColumnInfo(name = LocalMessageTable.MESSAGE_TIMESTAMP, index = true, defaultValue = "0") val timestamp: Long = 0,
    @ColumnInfo(name = LocalMessageTable.MESSAGE_CONTENT, defaultValue = "\'\'") val messageContent: String,
    @ColumnInfo(name = LocalMessageTable.MESSAGE_EXTENSIONS, defaultValue = "null") val extensions: String? = null
)
