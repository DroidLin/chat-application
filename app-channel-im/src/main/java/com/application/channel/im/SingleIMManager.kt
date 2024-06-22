package com.application.channel.im

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author liuzhongao
 * @since 2024/6/22 20:16
 */
object SingleIMManager {

    private val _state = MutableStateFlow(value = SingleIMState())

    val connectionService = MsgConnectionService()
    val msgService = MsgService()

    fun initService(initConfig: IMInitConfig) {
        if (this.connectionService.initService(initConfig)) {
            this._state.update { it.copy(state = State.NotConnected) }
        }
    }

    fun startService() {
        this.connectionService.startService()
    }

    fun stopService() {
        this.connectionService.stopService()
    }

    fun release() {
        this.connectionService.release()
        this._state.update { it.copy(state = State.NotInitialized) }
    }
}

data class SingleIMState(
    val state: State = State.NotInitialized
)

enum class State {
    NotInitialized,
    NotConnected,
    Connecting,
    Connected,
    NotAuthorized,
    Authorized,
}