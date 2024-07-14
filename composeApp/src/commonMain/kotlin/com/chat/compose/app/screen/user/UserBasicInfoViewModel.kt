package com.chat.compose.app.screen.user

import androidx.lifecycle.ViewModel
import com.application.channel.message.SessionType
import com.chat.compose.app.usecase.UpdateSessionContactUserBasicInfoUseCase
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
    private val updateSessionContactUserBasicInfoUseCase: UpdateSessionContactUserBasicInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserBasicInfoUiState())
    val uiState = this._uiState.asStateFlow()

    suspend fun fetchUserInfo(userId: Long) {
        this._uiState.update { it.copy(isLoading = true) }
        val profile = this.fetchUserInfoUseCase.fetchProfile(userId = userId)
        if (profile != null) {
            this._uiState.update {
                it.copy(
                    userName = profile.userInfo?.userName ?: "",
                    userId = profile.userInfo?.userId ?: -1,
                    sessionId = profile.sessionInfo?.sessionId ?: "",
                    sessionType = profile.sessionInfo?.sessionType ?: SessionType.Unknown
                )
            }
        }
        this._uiState.update { it.copy(isLoading = false) }
    }

}

data class UserBasicInfoUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val userId: Long = -1,
    val sessionId: String = "",
    val sessionType: SessionType = SessionType.Unknown
)