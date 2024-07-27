package com.chat.compose.app.platform.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chat.compose.app.metadata.isValid
import com.chat.compose.app.services.ProfileService
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

    AnimatedContent(
        targetState = isLogin,
        contentAlignment = Alignment.Center,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) {
        if (it) {
            AppScreen(modifier = Modifier.fillMaxSize())
        } else LoginLogicScreen(modifier = Modifier.fillMaxSize())
    }
}