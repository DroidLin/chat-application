package com.chat.compose.app.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.compose.app.network.isSuccess
import com.chat.compose.app.services.ProfileService
import com.chat.compose.app.usecase.network.FetchUserInfoUseCase
import com.chat.compose.app.usecase.network.RegistrationUseCase
import com.chat.compose.app.usecase.network.UserAccountCheckForRegistrationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author liuzhongao
 * @since 2024/6/25 22:08
 */
class RegisterAccountViewModel(
    private val registrationUseCase: RegistrationUseCase,
    private val fetchUseInfoUseCase: FetchUserInfoUseCase,
    private val userAccountCheckForRegistrationUseCase: UserAccountCheckForRegistrationUseCase,
    private val profileService: ProfileService
) : ViewModel() {

    fun register(userAccount: String, userName: String, password: String, onSuccess: () -> Unit) {
        this.viewModelScope.launch {
            this@RegisterAccountViewModel.registerInner(userAccount, userName, password, onSuccess)
        }
    }

    private suspend fun registerInner(userAccount: String, userName: String, password: String, onSuccess: () -> Unit) {
        val userAccountCheckResult = this.userAccountCheckForRegistrationUseCase.checkUseAccountAvailable(userAccount)
        if (!userAccountCheckResult.isSuccess) {
            return
        }
        val registrationResult = this.registrationUseCase.registration(userAccount, userName, password)
        if (!registrationResult.isSuccess) {
            return
        }
        this.profileService.refreshProfile()
        onSuccess()
    }
}