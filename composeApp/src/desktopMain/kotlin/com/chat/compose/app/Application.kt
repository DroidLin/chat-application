package com.chat.compose.app

import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.application
import com.chat.compose.app.metadata.ApplicationConfiguration
import com.chat.compose.app.metadata.isValid
import com.chat.compose.app.metadata.rememberAppConfiguration
import com.chat.compose.app.services.ProfileService
import com.chat.compose.app.ui.AppSafeArea
import com.chat.compose.app.ui.LocalAppSafeArea
import org.koin.compose.koinInject

fun main() {
    val appSafeArea = AppSafeArea()
    initEntryPoint()
    application {
        val appConfiguration = rememberAppConfiguration()
        CompositionLocalProvider(
            LocalApplicationConfiguration provides appConfiguration,
            LocalAppSafeArea provides appSafeArea,
        ) {
            val profileState = koinInject<ProfileService>().profileFlow.collectAsState()
            val isLogin = remember { derivedStateOf { profileState.value.isValid } }
            if (isLogin.value) {
                AppWindow(::exitApplication)
            } else LoginWindow(::exitApplication)
        }
    }
}

val LocalWindow = compositionLocalOf<ComposeWindow> { error("not provided") }
val LocalApplicationConfiguration = compositionLocalOf<ApplicationConfiguration> { error("not provided") }