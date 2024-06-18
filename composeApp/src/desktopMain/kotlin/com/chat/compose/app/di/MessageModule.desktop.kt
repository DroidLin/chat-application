package com.chat.compose.app.di

import com.application.channel.im.IMInitConfig
import com.application.channel.im.Token

/**
 * @author liuzhongao
 * @since 2024/6/18 23:43
 */

actual fun imInitConfig(): IMInitConfig {
    return IMInitConfig(
        remoteAddress = "",
        token = Token("", ""),
        factory = { null }
    )
}