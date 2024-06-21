package com.chat.compose.app.screen.message.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.compose.app.usecase.FetchSessionListUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * @author liuzhongao
 * @since 2024/6/18 00:55
 */
class SessionListViewModel constructor(
    private val fetchSessionListUseCase: FetchSessionListUseCase
) : ViewModel() {

    val sessionList = this.fetchSessionListUseCase.sessionList
        .stateIn(this.viewModelScope, SharingStarted.Lazily, emptyList())
}
