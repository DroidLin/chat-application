package com.chat.compose.app.screen.message.vm

import androidx.lifecycle.ViewModel
import com.chat.compose.app.usecase.network.FetchUserInfoUseCase
import com.chat.compose.app.usecase.FetchSessionListUseCase
import com.chat.compose.app.usecase.UpdateSessionContactUserBasicInfoUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

/**
 * @author liuzhongao
 * @since 2024/6/18 00:55
 */
class SessionListViewModel constructor(
    private val fetchSessionListUseCase: FetchSessionListUseCase,
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val updateSessionContactUserBasicInfoUseCase: UpdateSessionContactUserBasicInfoUseCase
) : ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    val sessionList = this.fetchSessionListUseCase.sessionList
        .stateIn(this.coroutineScope, SharingStarted.Lazily, emptyList())

    suspend fun updateSessionContactInfo() {
        val sessionIdList = this.sessionList.first { it.isNotEmpty() }
            .map { it.sessionId }
        val profileList = this.fetchUserInfoUseCase.fetchProfileBySessionId(sessionIdList)
        this.updateSessionContactUserBasicInfoUseCase.updateContactUserInfo(profileList)
    }
}
