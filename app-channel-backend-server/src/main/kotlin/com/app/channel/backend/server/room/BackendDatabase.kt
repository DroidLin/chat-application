package com.app.channel.backend.server.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.channel.backend.server.room.dao.AccountDao
import com.app.channel.backend.server.room.dao.ChatRoomDao
import com.app.channel.backend.server.room.dao.SessionInfoDao
import com.app.channel.backend.server.room.dao.UserInfoDao
import com.app.channel.backend.server.room.metadata.LocalAccount
import com.app.channel.backend.server.room.metadata.LocalChatRoomInfo
import com.app.channel.backend.server.room.metadata.LocalSessionInfo
import com.app.channel.backend.server.room.metadata.LocalUserInfo

/**
 * @author liuzhongao
 * @since 2024/6/26 23:33
 */
@Database(
    entities = [
        LocalUserInfo::class,
        LocalAccount::class,
        LocalSessionInfo::class,
        LocalChatRoomInfo::class,
    ],
    version = 2
)
abstract class BackendDatabase : RoomDatabase() {

    abstract val userInfoDao: UserInfoDao

    abstract val sessionInfoDao: SessionInfoDao

    abstract val accountDao: AccountDao

    abstract val chatRoomDao: ChatRoomDao
}