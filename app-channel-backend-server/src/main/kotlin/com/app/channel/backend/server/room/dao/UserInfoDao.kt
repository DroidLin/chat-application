package com.app.channel.backend.server.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.channel.backend.server.room.metadata.LocalUserInfo

/**
 * @author liuzhongao
 * @since 2024/6/26 23:17
 */
@Dao
interface UserInfoDao {

    @Insert
    suspend fun insertUserInfo(localUserInfo: LocalUserInfo)

    @Query("select * from local_user_info where user_id = :userId")
    suspend fun fetchUserInfo(userId: Long): LocalUserInfo?

    @Query("select * from local_user_info where account_id = :userAccount")
    suspend fun fetchUserInfoByAccount(userAccount: String): LocalUserInfo?
}