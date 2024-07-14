package com.application.channel.database.dao

import androidx.room.*
import com.application.channel.database.meta.LocalMessage
import kotlinx.coroutines.flow.Flow

/**
 * @author liuzhongao
 * @since 2024/6/8 19:32
 */
@Dao
interface LocalMessageDao {

    @Upsert
    suspend fun upsertMessage(message: LocalMessage)

    @Upsert
    suspend fun upsertMessages(messageList: List<LocalMessage>)

    @Update
    suspend fun updateMessage(message: LocalMessage)

    @Query("select * from table_message_record where message_uuid = :uuid and message_session_type_code = :sessionTypeCode limit 1")
    suspend fun fetchMessageByUUid(uuid: String, sessionTypeCode: Int): LocalMessage?

    @Query("select * from table_message_record where message_uuid in (:uuidList) and message_session_type_code = :sessionTypeCode")
    suspend fun fetchMessages(uuidList: List<String>, sessionTypeCode: Int): List<LocalMessage>

    @Query(
        "select * from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "and message_timestamp = :timestamp " +
                "order by message_id desc"
    )
    suspend fun fetchMessagesAtTime(
        sessionId: String,
        chatterSessionId: String,
        sessionTypeCode: Int,
        timestamp: Long,
    ): List<LocalMessage>

    @Query(
        "select * from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "and message_id = :id " +
                "order by message_id desc"
    )
    suspend fun fetchMessagesAtTimeWithId(
        sessionId: String,
        chatterSessionId: String,
        sessionTypeCode: Int,
        id: Int,
    ): List<LocalMessage>

    @Query(
        "select * from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "and message_timestamp < :timestamp " +
                "order by message_timestamp desc, message_id desc limit :limit"
    )
    suspend fun fetchMessages(
        sessionId: String,
        chatterSessionId: String,
        sessionTypeCode: Int,
        timestamp: Long,
        limit: Int,
    ): List<LocalMessage>

    @Query(
        "select * from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "and message_timestamp <= :timestamp " +
                "and message_id < :id " +
                "order by message_timestamp desc, message_id desc limit :limit"
    )
    suspend fun fetchMessagesWithId(
        sessionId: String,
        chatterSessionId: String,
        sessionTypeCode: Int,
        id: Int,
        timestamp: Long,
        limit: Int,
    ): List<LocalMessage>

    @Query(
        "select * from (select * from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "and message_timestamp >= :timestamp " +
                "order by message_timestamp, message_id limit :limit) order by message_timestamp desc, message_id desc"
    )
    suspend fun fetchMessagesDesc(
        sessionId: String,
        chatterSessionId: String,
        sessionTypeCode: Int,
        timestamp: Long,
        limit: Int,
    ): List<LocalMessage>

    @Query(
        "select * from (select * from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "and message_timestamp >= :timestamp " +
                "and message_id > :id " +
                "order by message_timestamp, message_id limit :limit) order by message_timestamp desc, message_id desc"
    )
    suspend fun fetchMessagesDescWithId(
        sessionId: String,
        chatterSessionId: String,
        sessionTypeCode: Int,
        id: Int,
        timestamp: Long,
        limit: Int,
    ): List<LocalMessage>

    @Query(
        "select count(*) from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "and message_state_user_consumed = 1"
    )
    suspend fun calculateUserSessionUnreadCount(sessionId: String, chatterSessionId: String, sessionTypeCode: Int): Int

    @Query(
        "select count(*) from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "and message_state_user_consumed = 1"
    )
    fun observableCalculateUserSessionUnreadCount(sessionId: String, chatterSessionId: String, sessionTypeCode: Int): Flow<Int>

    @Query(
        "select * from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "order by message_timestamp desc limit 1"
    )
    suspend fun queryRecentMessageWithSomeone(
        sessionId: String,
        chatterSessionId: String,
        sessionTypeCode: Int
    ): LocalMessage?

    @Query(
        "select * from table_message_record where ((message_sender_session_id = :sessionId and message_receiver_session_id = :chatterSessionId) or (message_sender_session_id = :chatterSessionId and message_receiver_session_id = :sessionId)) " +
                "and message_session_type_code = :sessionTypeCode " +
                "order by message_timestamp desc limit 1"
    )
    fun observableRecentMessageWithSomeone(
        sessionId: String,
        chatterSessionId: String,
        sessionTypeCode: Int
    ): Flow<LocalMessage?>

    @Delete
    suspend fun deleteMessages(vararg messages: LocalMessage)

    @Delete
    suspend fun deleteMessages(messagesToDelete: List<LocalMessage>)
}