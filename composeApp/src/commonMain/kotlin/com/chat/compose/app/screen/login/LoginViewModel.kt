package com.chat.compose.app.screen.login

import androidx.lifecycle.viewModelScope
import com.chat.compose.app.lifecycle.ApplicationLifecycleRegistry
import com.chat.compose.app.metadata.isValid
import com.chat.compose.app.network.isSuccess
import com.chat.compose.app.platform.viewmodel.AbstractStatefulViewModel
import com.chat.compose.app.platform.viewmodel.Event
import com.chat.compose.app.platform.viewmodel.State
import com.chat.compose.app.services.ProfileService
import com.chat.compose.app.usecase.network.LoginUserUseCase
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/25 00:24
 */
class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val profileService: ProfileService
) : AbstractStatefulViewModel<LoginState, LoginEvent>() {

    override val initialState: LoginState get() = LoginState()

    fun updateUserName(inputText: String) {
        if (inputText.isBlank() || inputText.isEmpty()) {
            this.updateState {
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
            this.updateState {
                it.copy(
                    inputUserName = it.inputUserName.copy(
                        isError = true,
                        errorMessage = "用户名不可包含非法字符！"
                    )
                )
            }
            return
        }
        this.updateState { it.copy(inputUserName = InputTextState(inputText)) }
    }

    fun launchLogin(onComplete: () -> Unit) {
        val userAccount = this.state.inputUserName.inputText
        val password = this.state.inputPassword.inputText
        this.updateState { it.copy(isLoading = true) }
        this.viewModelScope.launch {
            val loginResult = this@LoginViewModel.loginUserUseCase.loginUserAccount(userAccount, password)
            if (loginResult.isSuccess && this@LoginViewModel.profileService.refreshProfile().isValid) {
                ApplicationLifecycleRegistry.onUserLogin(this@LoginViewModel.profileService.profile)
                onComplete()
            }
            if (!loginResult.isSuccess) {
                val message = loginResult.message ?: "未知错误"
                emitEvent { LoginEvent.LoginFailureEvent(message) }
            }
            if (!this@LoginViewModel.profileService.profile.isValid) {
                emitEvent { LoginEvent.LoginFailureEvent("获取用户信息失败！") }
            }
            this@LoginViewModel.updateState { it.copy(isLoading = false) }
        }
    }

    fun updatePassword(inputText: String) {
        this.updateState { it.copy(inputPassword = it.inputPassword.copy(inputText = inputText)) }
    }

    fun updateShowPassword(showPassword: Boolean) {
        this.updateState { it.copy(showRawPassword = showPassword) }
    }

    companion object {
        private val userNameCheckRegex = Regex("[a-zA-Z0-9@._-]+")
    }
}

sealed interface LoginEvent : Event {

    data class LoginFailureEvent(
        val message: String
    ) : LoginEvent
}

data class LoginState(
    val isLoading: Boolean = false,
    val inputUserName: InputTextState = InputTextState(),
    val inputPassword: InputTextState = InputTextState(),
    val showRawPassword: Boolean = false,
) : State.Success

data class InputTextState(
    val inputText: String = "",
    val isError: Boolean = false,
    val errorMessage: String = ""
)