package com.chat.compose.app.metadata

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * @author liuzhongao
 * @since 2024/6/16 12:16
 */
@Stable
class WindowConfiguration(
    title: String = "",
    isDarkMode: Boolean = false,
) {
    var title: String by mutableStateOf(title)
    var isDarkMode: Boolean by mutableStateOf(isDarkMode)
}
