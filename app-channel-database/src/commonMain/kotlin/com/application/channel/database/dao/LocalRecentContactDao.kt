package com.application.channel.database.dao

import androidx.room.*
import com.application.channel.database.meta.LocalRecentContact
import kotlinx.coroutines.flow.Flow

/**
 * @author liuzhongao
 * @since 2024/7/24 22:30
 */
@Dao
interface LocalRecentContactDao {

    @Upsert
    suspend fun upsert(contact: LocalRecentContact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: LocalRecentContact)

    @Delete
    suspend fun delete(contact: LocalRecentContact)

    @Update
    suspend fun update(contact: LocalRecentContact)

    @Query("SELECT * FROM table_recent_contact WHERE recent_session_id = :sessionId and recent_session_type_code = :sessionTypeCode limit 1")
    suspend fun fetchRecentContact(sessionId: String, sessionTypeCode: Int): LocalRecentContact?

    @Query("SELECT * FROM table_recent_contact limit :limit")
    suspend fun fetchRecentContactList(limit: Int): List<LocalRecentContact>

    @Query("SELECT * FROM table_recent_contact limit :limit")
    fun fetchObservableRecentContactList(limit: Int): Flow<List<LocalRecentContact>>

    @Query("SELECT * FROM table_recent_contact where recent_session_id = :sessionId and recent_session_type_code = :sessionTypeCode limit 1")
    fun fetchObservableRecentContact(sessionId: String, sessionTypeCode: Int): Flow<LocalRecentContact?>
}