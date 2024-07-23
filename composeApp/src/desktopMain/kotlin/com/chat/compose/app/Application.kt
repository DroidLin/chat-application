package com.chat.compose.app

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.application
import com.chat.compose.app.metadata.ApplicationConfiguration
import com.chat.compose.app.metadata.rememberAppConfiguration
import com.chat.compose.app.ui.AppSafeArea
import com.chat.compose.app.ui.LocalAppSafeArea

fun main() {
    val appSafeArea = AppSafeArea()
    initEntryPoint()
    application {
        val appConfiguration = rememberAppConfiguration()
        CompositionLocalProvider(
            LocalApplicationConfiguration provides appConfiguration,
            LocalAppSafeArea provides appSafeArea,
        ) {
            AppWindow(::exitApplication)
        }
    }
}

val LocalWindow = compositionLocalOf<ComposeWindow> { error("not provided") }
val LocalApplicationConfiguration = compositionLocalOf<ApplicationConfiguration> { error("not provided") }