package com.chat.compose.app.screen.user

import androidx.lifecycle.ViewModel
import com.chat.compose.app.services.ProfileService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author liuzhongao
 * @since 2024/7/14 11:07
 */
class PersonalInfoViewModel(private val profileService: ProfileService) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalInfoUiState())
    val uiState = this._uiState.asStateFlow()

    val userProfile = this.profileService.profileFlow
}

data class PersonalInfoUiState(
    val isLogin: Boolean = false,
)