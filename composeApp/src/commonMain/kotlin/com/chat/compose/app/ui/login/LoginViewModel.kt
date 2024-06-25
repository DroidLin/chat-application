package com.chat.compose.app.ui.login

import androidx.lifecycle.ViewModel
import com.chat.compose.app.storage.MutableMapStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author liuzhongao
 * @since 2024/6/25 00:24
 */
class LoginViewModel : ViewModel() {

    private val mutablePreference = MutableMapStorage("loginPreference.json")

    private val _state = MutableStateFlow(LoginState())
    val state = this._state.asStateFlow()

    fun updateUserName(inputText: String) {
        if (inputText.isBlank() || inputText.isEmpty()) {
            this._state.update {
                it.copy(
                    inputUserName = it.inputUserName.copy(
                        inputText = inputText,
                        isError = true,
                        errorMessage = "用户名不可为空！"
                    )
                )
            }
            return
        } else if (!inputText.matches(userNameCheckRegex)) {
            this._state.update {
                it.copy(
                    inputUserName = it.inputUserName.copy(
                        isError = true,
                        errorMessage = "用户名不可包含非法字符！"
                    )
                )
            }
            return
        }
        this._state.update { it.copy(inputUserName = InputTextState(inputText)) }
    }

    fun updatePassword(inputText: String) {
        this._state.update { it.copy(inputPassword = it.inputPassword.copy(inputText = inputText)) }
    }

    fun updateShowPassword(showPassword: Boolean) {
        this._state.update { it.copy(showRawPassword = showPassword) }
    }

    companion object {
        private val userNameCheckRegex = Regex("[a-zA-Z0-9@._-]+")
    }
}

data class LoginState(
    val isLoading: Boolean = false,
    val inputUserName: InputTextState = InputTextState(),
    val inputPassword: InputTextState = InputTextState(),
    val showRawPassword: Boolean = false,
)

data class InputTextState(
    val inputText: String = "",
    val isError: Boolean = false,
    val errorMessage: String = ""
)