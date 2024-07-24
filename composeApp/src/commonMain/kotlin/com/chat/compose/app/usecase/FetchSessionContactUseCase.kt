package com.chat.compose.app.usecase

import com.application.channel.im.MsgService
import com.application.channel.message.SessionType
import com.application.channel.message.metadata.RecentContact
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

    suspend fun fetchRecentContact(sessionId: String, sessionType: SessionType): RecentContact? {
        return this.msgService.fetchRecentContact(sessionId, sessionType)
    }

    suspend fun fetchRecentContactOrCreate(sessionId: String, sessionType: SessionType): RecentContact? {
        val sessionContact = this.msgService.fetchRecentContact(sessionId, sessionType)
        if (sessionContact == null) {
            this@FetchSessionContactUseCase.msgService.insertRecentContact(sessionId, sessionType)
            val profileList = this@FetchSessionContactUseCase.fetchUserInfoUseCase.fetchProfileBySessionId(listOf(sessionId))
            this@FetchSessionContactUseCase.updateSessionContactUserBasicInfoUseCase.updateContactUserInfo(profileList)
        }
        return this.msgService.fetchRecentContact(sessionId, sessionType)
    }

    fun fetchObservableRecentContact(sessionId: String, sessionType: SessionType): Flow<RecentContact?> {
        return this.msgService.fetchRecentContactFlow(sessionId, sessionType)
    }

    fun fetchObservableRecentContactOrCreate(sessionId: String, sessionType: SessionType): Flow<RecentContact> {
        return flow {
            val sessionContact = this@FetchSessionContactUseCase.fetchRecentContact(sessionId, sessionType)
            if (sessionContact == null) {
                this@FetchSessionContactUseCase.msgService.insertSessionContact(sessionId, sessionType)
                val profileList = this@FetchSessionContactUseCase.fetchUserInfoUseCase.fetchProfileBySessionId(listOf(sessionId))
                this@FetchSessionContactUseCase.updateSessionContactUserBasicInfoUseCase.updateContactUserInfo(profileList)
            }
            this@FetchSessionContactUseCase.fetchObservableRecentContact(sessionId, sessionType)
                .filterNotNull()
                .collect(this)
        }
    }
}