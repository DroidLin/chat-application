package com.application.channel.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.application.channel.database.dao.LocalMessageDao
import com.application.channel.database.dao.LocalRecentContactDao
import com.application.channel.database.dao.LocalSessionContactDao
import com.application.channel.database.meta.LocalMessage
import com.application.channel.database.meta.LocalRecentContact
import com.application.channel.database.meta.LocalSessionContact

/**
 * @author liuzhongao
 * @since 2024/6/8 18:13
 */

@Database(
    entities = [
        LocalMessage::class,
        LocalSessionContact::class,
        LocalRecentContact::class
    ],
    version = 1
)
internal abstract class MessageDatabase : RoomDatabase() {

    abstract val messageDao: LocalMessageDao

    abstract val sessionDao: LocalSessionContactDao

    abstract val recentContactDao: LocalRecentContactDao
}
