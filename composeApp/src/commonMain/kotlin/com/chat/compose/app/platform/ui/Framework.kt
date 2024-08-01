package com.chat.compose.app.platform.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chat.compose.app.metadata.isValid
import com.chat.compose.app.services.ProfileService
import com.chat.compose.app.ui.ProfileProviderScope
import com.chat.compose.app.ui.SharedElementLayoutProvider
import org.koin.compose.koinInject

/**
 * @author liuzhongao
 * @since 2024/7/17 00:17
 */
@Composable
fun FrameworkScreen() {
    val profileState = koinInject<ProfileService>().profileFlow.collectAsStateWithLifecycle()
    val isLogin by remember {
        derivedStateOf { profileState.value.isValid }
    }

    if (isLogin) {
        val profile by profileState
        profile.ProfileProviderScope {
            SharedElementLayoutProvider {
                AppScreen(modifier = Modifier.fillMaxSize())
            }
        }
    } else LoginLogicScreen(modifier = Modifier.fillMaxSize())
}