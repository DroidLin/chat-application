package com.app.channel.backend.server.room

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.app.channel.backend.server.room.dao.AccountDao
import com.app.channel.backend.server.room.dao.SessionInfoDao
import com.app.channel.backend.server.room.dao.UserInfoDao
import java.io.File

/**
 * @author liuzhongao
 * @since 2024/6/26 23:39
 */
interface AppBackendDatabase {
    val userInfoDao: UserInfoDao
    val sessionInfoDao: SessionInfoDao
    val accountDao: AccountDao
}

fun AppBackendDatabase(): AppBackendDatabase {
    val filePath = File("database${File.separator}backend.db").absolutePath
    val database = Room.databaseBuilder<BackendDatabase>(filePath)
        .setDriver(BundledSQLiteDriver())
        .build()
    return AppBackendDatabaseImpl(database)
}

private class AppBackendDatabaseImpl(
    private val database: BackendDatabase
) : AppBackendDatabase {
    override val userInfoDao: UserInfoDao get() = this.database.userInfoDao
    override val sessionInfoDao: SessionInfoDao get() = this.database.sessionInfoDao
    override val accountDao: AccountDao get() = this.database.accountDao
}