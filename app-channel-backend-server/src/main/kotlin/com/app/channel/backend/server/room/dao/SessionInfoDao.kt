package com.app.channel.backend.server.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.channel.backend.server.room.metadata.LocalSessionInfo

/**
 * @author liuzhongao
 * @since 2024/6/26 23:35
 */
@Dao
interface SessionInfoDao {

    @Insert
    suspend fun insertSessionInfo(sessionInfo: LocalSessionInfo)

    @Query("SELECT * FROM local_session_info WHERE account_id = :accountId limit 1")
    suspend fun fetchSessionInfo(accountId: String): LocalSessionInfo?
}