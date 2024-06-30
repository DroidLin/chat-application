package com.chat.compose.app.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.compose.app.lifecycle.ApplicationLifecycleRegistry
import com.chat.compose.app.metadata.isValid
import com.chat.compose.app.network.isSuccess
import com.chat.compose.app.services.ProfileService
import com.chat.compose.app.storage.MutableMapStorage
import com.chat.compose.app.usecase.network.LoginUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/25 00:24
 */
class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val profileService: ProfileService
) : ViewModel() {

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

    fun launchLogin(onComplete: () -> Unit) {
        val userAccount = this._state.value.inputUserName.inputText
        val password = this._state.value.inputPassword.inputText
        this._state.update { it.copy(isLoading = true) }
        this.viewModelScope.launch {
            val loginResult = this@LoginViewModel.loginUserUseCase.loginUserAccount(userAccount, password)
            if (loginResult.isSuccess && this@LoginViewModel.profileService.refreshProfile().isValid) {
                ApplicationLifecycleRegistry.onUserLogin(this@LoginViewModel.profileService.profile)
                onComplete()
            }
            this@LoginViewModel._state.update { it.copy(isLoading = false) }
        }
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