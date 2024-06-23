package com.chat.compose.app.metadata

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.application.channel.im.IMInitConfig
import com.application.channel.im.Token
import com.application.channel.message.Account

/**
 * @author liuzhongao
 * @since 2024/6/16 12:16
 */
@Stable
class ApplicationConfiguration(
    title: String = "",
    isDarkMode: Boolean = false,
) {
    var title: String by mutableStateOf(title)
    var isDarkMode: Boolean by mutableStateOf(isDarkMode)

    var initConfig: IMInitConfig by mutableStateOf(
        IMInitConfig(
            remoteAddress = "127.0.0.1",
            account = Account(sessionId = "", accountId = ""),
        )
    )
}
