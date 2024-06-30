package com.chat.compose.app.services

import com.chat.compose.app.metadata.Profile
import com.chat.compose.app.usecase.network.FetchUserInfoUseCase
import com.chat.compose.app.storage.MutableMapStorage
import com.chat.compose.app.util.PreferenceCenter
import com.chat.compose.app.util.fromJson
import com.chat.compose.app.util.toJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/30 02:41
 */
interface ProfileService {

    val profileFlow: StateFlow<Profile>

    val profile: Profile

    suspend fun refreshProfile(): Profile

    fun refreshProfileAsync()
}

fun ProfileService(fetchUserInfoUserCase: FetchUserInfoUseCase): ProfileService {
    return ProfileServiceImpl(fetchUserInfoUserCase)
}

private class ProfileServiceImpl(
    private val fetchUserInfoUserCase: FetchUserInfoUseCase,
) : ProfileService {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _preference by PreferenceCenter.loginPreference
    private val _profileFlow = MutableStateFlow(Profile())

    override val profileFlow: StateFlow<Profile> = this._profileFlow.asStateFlow()
    override val profile: Profile get() = this._profileFlow.value

    init {
        val profileString = this._preference.getString(KEY_PROFILE, "")
        if (!profileString.isNullOrEmpty()) {
            val profile = fromJson<Profile>(profileString)
            if (profile != null) {
                this._profileFlow.update { profile }
            }
        }
    }

    override suspend fun refreshProfile(): Profile {
        val profile = this.fetchUserInfoUserCase.fetchProfile()
        return if (profile != null) {
            this._preference.putString(KEY_PROFILE, profile.toJson())
            this._preference.flush()
            this._profileFlow.updateAndGet { profile }
        } else this.profile
    }

    override fun refreshProfileAsync() {
        this.coroutineScope.launch {
            this@ProfileServiceImpl.refreshProfile()
        }
    }

    companion object {
        const val KEY_PROFILE = "KEY_PROFILE"
    }
}