package com.chat.compose.app.usecase

import com.application.channel.im.MsgService
import com.application.channel.message.SessionType
import com.application.channel.message.metadata.SessionContact
import com.chat.compose.app.usecase.network.FetchUserInfoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

/**
 * @author liuzhongao
 * @since 2024/6/20 21:43
 */
class FetchSessionContactUseCase(
    private val msgService: MsgService,
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val updateSessionContactUserBasicInfoUseCase: UpdateSessionContactUserBasicInfoUseCase
) {

    suspend fun fetchSessionContact(sessionId: String, sessionType: SessionType): SessionContact? {
        return this.msgService.fetchSessionContact(sessionId, sessionType)
    }

    suspend fun fetchSessionContactOrCreate(sessionId: String, sessionType: SessionType): SessionContact? {
        val sessionContact = this.msgService.fetchSessionContact(sessionId, sessionType)
        if (sessionContact == null) {
            this@FetchSessionContactUseCase.msgService.insertSessionContact(sessionId, sessionType)
            val profileList = this@FetchSessionContactUseCase.fetchUserInfoUseCase.fetchProfileBySessionId(listOf(sessionId))
            this@FetchSessionContactUseCase.updateSessionContactUserBasicInfoUseCase.updateSessionContactUserInfo(profileList)
        }
        return this.msgService.fetchSessionContact(sessionId, sessionType)
    }

    fun fetchObservableSessionContact(sessionId: String, sessionType: SessionType): Flow<SessionContact?> {
        return this.msgService.fetchObservableSessionContact(sessionId, sessionType)
    }

    fun fetchObservableSessionContactOrCreate(sessionId: String, sessionType: SessionType): Flow<SessionContact> {
        return flow {
            val sessionContact = this@FetchSessionContactUseCase.fetchSessionContact(sessionId, sessionType)
            if (sessionContact == null) {
                this@FetchSessionContactUseCase.msgService.insertSessionContact(sessionId, sessionType)
                val profileList = this@FetchSessionContactUseCase.fetchUserInfoUseCase.fetchProfileBySessionId(listOf(sessionId))
                this@FetchSessionContactUseCase.updateSessionContactUserBasicInfoUseCase.updateSessionContactUserInfo(profileList)
            }
            this@FetchSessionContactUseCase.fetchObservableSessionContact(sessionId, sessionType)
                .filterNotNull()
                .collect(this)
        }
    }
}