package com.chat.compose.app.metadata

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import com.application.channel.im.IMInitConfig
import com.application.channel.message.Account

/**
 * @author liuzhongao
 * @since 2024/6/16 12:16
 */
@Composable
fun rememberAppConfiguration(): ApplicationConfiguration {
    val isSystemInDarkMode = isSystemInDarkTheme()
    return remember(isSystemInDarkMode) {
        ApplicationConfiguration(isDarkMode = isSystemInDarkMode)
    }
}

@Stable
class ApplicationConfiguration(
    title: String = "",
    isDarkMode: Boolean = false,
    alwaysOnTop: Boolean = false,
) {
    var title: String by mutableStateOf(title)
    var isDarkMode: Boolean by mutableStateOf(isDarkMode)
    var alwaysOnTop: Boolean by mutableStateOf(alwaysOnTop)
}
