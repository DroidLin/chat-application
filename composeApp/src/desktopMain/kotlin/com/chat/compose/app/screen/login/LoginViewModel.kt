package com.chat.compose.app.screen.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author liuzhongao
 * @since 2024/6/25 00:24
 */
class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = this._state.asStateFlow()

    fun updateUserName(inputText: String) {
        this._state.update { it.copy(inputUserName = inputText) }
    }

    fun updatePassword(inputText: String) {
        this._state.update { it.copy(inputPassword = inputText) }
    }

    fun updateShowPassword(showPassword: Boolean) {
        this._state.update { it.copy(showRawPassword = showPassword) }
    }

}

data class LoginState(
    val inputUserName: String = "",
    val inputPassword: String = "",
    val showRawPassword: Boolean = false,
)