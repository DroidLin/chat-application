package com.application.channel.im

import com.application.channel.core.model.ChannelContext
import com.application.channel.message.FailureType
import com.application.channel.message.LogoutReason
import com.application.channel.message.SimpleChatServiceEventObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author liuzhongao
 * @since 2024/6/23 20:13
 */
class ReConnectProcessor(
    private val maxReConnectCount: Int,
    private val function: () -> Unit
) : SimpleChatServiceEventObserver() {

    private val _innerState = MutableStateFlow(State.NotInitialized)
    val state: StateFlow<State> = this._innerState.asStateFlow()

    val currentState: State get() = this.state.value

    private var failureCountUntilLastSuccess: Int = 0

    override fun onDatabaseInitialized() {
        if (this.currentState < State.NotConnected) {
            this._innerState.update { State.NotConnected }
        }
    }

    override fun onStartConnect() {
        this._innerState.update { State.Connecting }
    }

    override fun onLogin() {
        this._innerState.update { State.Authorized }
    }

    override fun onLoginFailure(event: FailureType, message: String?) {
        this._innerState.update { State.NotAuthorized }
        when (event) {
            FailureType.LoginConnectFailure -> {
                this.tryReConnect()
            }
            else -> {}
        }
    }

    override fun onLogout(type: LogoutReason) {
        if (type == LogoutReason.LogoutUserTrigger) {
            this._innerState.update { State.NotConnected }
        } else this._innerState.update { State.NotAuthorized }
    }

    override fun onConnectionSuccess(channelCtx: ChannelContext) {
        this._innerState.update { State.Connected }
    }

    override fun onConnectionFailure(channelCtx: ChannelContext, throwable: Throwable?) {
        this._innerState.update { State.NotConnected }
        this.tryReConnect()
    }

    override fun onConnectionLost(channelCtx: ChannelContext, throwable: Throwable?) {
        this._innerState.update { State.NotConnected }
        this.tryReConnect()
    }

    private fun tryReConnect() {
        if (++this.failureCountUntilLastSuccess >= this.maxReConnectCount) {
            return
        }
        this.function()
    }
}