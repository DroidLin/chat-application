package com.application.channel.database.meta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.application.channel.database.LocalRecentContactTable
import com.application.channel.database.LocalSessionContactTable

/**
 * @author liuzhongao
 * @since 2024/7/24 22:30
 */
@Entity(
    tableName = LocalRecentContactTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalSessionContact::class,
            parentColumns = [LocalSessionContactTable.SESSION_ID],
            childColumns = [LocalRecentContactTable.RECENT_SESSION_ID]
        ),
        ForeignKey(
            entity = LocalSessionContact::class,
            parentColumns = [LocalSessionContactTable.SESSION_TYPE_CODE],
            childColumns = [LocalRecentContactTable.RECENT_SESSION_TYPE_CODE]
        )
    ]
)
data class LocalRecentContact(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = LocalRecentContactTable.RECENT_SESSION_ID, index = true) val sessionId: String,
    @ColumnInfo(name = LocalRecentContactTable.RECENT_SESSION_TYPE_CODE, index = true) val sessionTypeCode: Int,
    @ColumnInfo(name = LocalRecentContactTable.RECENT_TIMESTAMP) val recentTimestamp: Long,
    @ColumnInfo(name = LocalRecentContactTable.RECENT_EXTENSIONS) val extensions: String?,
)