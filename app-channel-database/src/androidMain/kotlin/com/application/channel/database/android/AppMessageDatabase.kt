package com.application.channel.database.android

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.MessageDatabase
import com.application.channel.database.databaseMigrations

/**
 * @author liuzhongao
 * @since 2024/6/8 21:31
 */
fun AppMessageDatabase(context: Context, sessionId: String): AppMessageDatabase {
    val databaseFile = context.getDatabasePath("app_message_database_${sessionId}.db")
    val instance = Room.databaseBuilder<MessageDatabase>(context.applicationContext, databaseFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .addMigrations(*databaseMigrations)
        .build()
    return AppMessageDatabase(sessionId, instance)
}