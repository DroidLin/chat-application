package com.chat.compose.app.platform.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * @author liuzhongao
 * @since 2024/7/31 23:47
 */
abstract class AbstractStatefulViewModel<S : State, E : Event> : ViewModel() {

    protected abstract val initialState: S

    private val _uiState by lazy { MutableStateFlow(this.initialState) }
    val uiState: StateFlow<S> by lazy { this._uiState.asStateFlow() }

    protected val state: S get() = this._uiState.value

    private val channelEvent by lazy { Channel<E>(capacity = Channel.UNLIMITED) }
    val eventFlow by lazy { this.channelEvent.receiveAsFlow() }

    protected fun updateState(function: (S) -> S) {
        this._uiState.update(function)
    }

    protected fun emitEvent(event: E) {
        val channelResult = this.channelEvent.trySend(event)
        if (channelResult.isFailure) {
            channelResult.exceptionOrNull()?.printStackTrace()
        }
    }

    protected suspend fun emitEvent(factory: suspend () -> E) {
        val event = factory()
        this.channelEvent.send(event)
    }
}

interface State {

    interface Error : State

    interface Loading : State

    interface Success : State
}

val State.isError: Boolean get() = this is State.Error
val State.isLoading: Boolean get() = this is State.Loading

interface Event