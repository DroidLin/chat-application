package com.application.channel.database.dao

import androidx.room.*
import com.application.channel.database.meta.LocalSessionContact
import kotlinx.coroutines.flow.Flow

/**
 * @author liuzhongao
 * @since 2024/6/8 20:08
 */
@Dao
interface LocalSessionContactDao {

    @Upsert
    suspend fun upsertSessionContact(sessionContact: LocalSessionContact)

    @Insert
    suspend fun insertSessionContact(sessionContact: LocalSessionContact)

    @Update
    suspend fun updateSessionContact(sessionContact: LocalSessionContact)

    @Delete
    suspend fun deleteSessionContact(sessionContact: LocalSessionContact)

    @Query("select * from table_session_contact where session_id = :sessionId and session_type_code = :sessionTypeCode limit 1")
    suspend fun fetchSessionContact(sessionId: String, sessionTypeCode: Int): LocalSessionContact?

    @Query("select * from table_session_contact where session_id = :sessionId and session_type_code = :sessionTypeCode limit 1")
    fun fetchObservableSessionContact(sessionId: String, sessionTypeCode: Int): Flow<LocalSessionContact?>

    @Query("select * from table_session_contact order by session_last_modify_time desc limit :limit")
    suspend fun fetchRecentSessionContactList(limit: Int): List<LocalSessionContact>

    @Query("select * from table_session_contact order by session_last_modify_time desc limit :limit")
    fun observableRecentSessionContacts(limit: Int = 10): Flow<List<LocalSessionContact>>
}