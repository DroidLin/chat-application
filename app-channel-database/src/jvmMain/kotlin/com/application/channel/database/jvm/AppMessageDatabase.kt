package com.application.channel.database.jvm

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.MessageDatabase
import com.application.channel.database.MessageDatabase_Impl
import com.application.channel.database.databaseMigrations

/**
 * @author liuzhongao
 * @since 2024/6/8 21:31
 */
fun getDatabase(filePath: String, sessionId: String): AppMessageDatabase {
    val instance =  Room.databaseBuilder<MessageDatabase>(name = filePath)
        .setDriver(BundledSQLiteDriver())
        .addMigrations(*databaseMigrations)
        .build()
    return AppMessageDatabase(sessionId, instance)
}