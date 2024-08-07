package com.app.channel.backend.server.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.channel.backend.server.room.metadata.LocalChatRoomInfo

/**
 * @author liuzhongao
 * @since 2024/8/2 16:51
 */
@Dao
interface ChatRoomDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(localChatRoomInfo: LocalChatRoomInfo)

    @Query("SELECT * FROM local_chat_room WHERE chat_room_session_id = :sessionId and chat_room_session_type = :sessionTypeCode limit 1")
    suspend fun fetchChatRoomInfo(sessionId: String, sessionTypeCode: Int): LocalChatRoomInfo?
}