package com.app.channel.backend.server.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.channel.backend.server.room.metadata.LocalAccount

/**
 * @author liuzhongao
 * @since 2024/6/26 23:35
 */
@Dao
interface AccountDao {

    @Insert
    suspend fun insertAccount(localAccount: LocalAccount)

    @Query("SELECT * FROM local_account WHERE account_id = :userAccount limit 1")
    suspend fun fetchAccountById(userAccount: String): LocalAccount?

    @Query("SELECT count(*) FROM local_account WHERE account_id = :userAccount")
    suspend fun checkCountOfUserAccount(userAccount: String): Int
}