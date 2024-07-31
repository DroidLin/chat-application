package com.chat.compose.app.screen.user

import com.application.channel.message.SessionType
import com.chat.compose.app.metadata.Profile
import com.chat.compose.app.platform.viewmodel.AbstractStatefulViewModel
import com.chat.compose.app.platform.viewmodel.Event
import com.chat.compose.app.platform.viewmodel.State
import com.chat.compose.app.usecase.*
import com.chat.compose.app.usecase.network.FetchUserInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author liuzhongao
 * @since 2024/7/13 01:14
 */
class UserBasicInfoViewModel(
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val fetchSessionContactUseCase: FetchSessionContactUseCase,
    private val fetchRecentContactUseCase: FetchRecentContactUseCase,
    private val updateSessionContactUserBasicInfoUseCase: UpdateSessionContactUserBasicInfoUseCase,
    private val insertSessionContactUseCase: InsertSessionContactUseCase,
    private val insertRecentContactUseCase: InsertRecentContactUseCase,
) : AbstractStatefulViewModel<UserBasicInfoState, UserBasicInfoEvent>() {

    override val initialState: UserBasicInfoState get() = UserBasicInfoState.Loading

    suspend fun fetchUserInfo(userId: Long) {
        this.updateState { UserBasicInfoState.Loading }
        val profile = this.fetchUserInfoUseCase.fetchProfile(userId = userId)
        if (profile != null) {
            val sessionId = profile.sessionInfo?.sessionId
            val sessionType = profile.sessionInfo?.sessionType
            if (!sessionId.isNullOrEmpty() && sessionType != null) {
                this.updateUserBasicContact(sessionId, sessionType, profile)
            }
            this.updateState {
                UserBasicInfoState.UserBasicInfoUiState(
                    userName = profile.userInfo?.userName ?: "",
                    userId = profile.userInfo?.userId ?: -1,
                    sessionId = profile.sessionInfo?.sessionId ?: "",
                    sessionType = profile.sessionInfo?.sessionType ?: SessionType.Unknown
                )
            }
        } else {
            this.updateState {
                UserBasicInfoState.Error(
                    message = "获取用户信息失败"
                )
            }
        }
    }

    private suspend fun updateUserBasicContact(sessionId: String, sessionType: SessionType, profile: Profile) {
        val profileList = listOf(profile)

        val sessionContactExist = (this.fetchSessionContactUseCase.fetchSessionContact(sessionId, sessionType) ?: run {
            this.insertSessionContactUseCase.insertSessionContact(sessionId, sessionType)
            this.fetchSessionContactUseCase.fetchSessionContact(sessionId, sessionType)
        }) != null
        if (sessionContactExist) {
            this.updateSessionContactUserBasicInfoUseCase.updateSessionContactProfile(profileList)
        }
        val recentContactExist = (this.fetchRecentContactUseCase.fetchRecentContact(sessionId, sessionType) ?: run {
            this.insertRecentContactUseCase.insertRecentContact(sessionId, sessionType)
            this.fetchRecentContactUseCase.fetchRecentContact(sessionId, sessionType)
        }) != null
        if (recentContactExist) {
            this.updateSessionContactUserBasicInfoUseCase.updateRecentContactProfile(profileList)
        }
    }
}

sealed interface UserBasicInfoState : State {

    data object Loading : UserBasicInfoState, State.Loading

    data class Error(
        val message: String
    ) : UserBasicInfoState, State.Error

    data class UserBasicInfoUiState(
        val userName: String = "",
        val userId: Long = -1,
        val sessionId: String = "",
        val sessionType: SessionType = SessionType.Unknown
    ) : UserBasicInfoState, State.Success
}

interface UserBasicInfoEvent : Event
