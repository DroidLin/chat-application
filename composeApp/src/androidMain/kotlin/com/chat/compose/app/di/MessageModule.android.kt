package com.chat.compose.app.di

import com.application.channel.database.android.AppMessageDatabase
import com.application.channel.im.IMInitConfig
import com.application.channel.im.Token
import com.chat.compose.app.BasicApplication
import com.chat.compose.app.usecase.sessionContactV1
import com.chat.compose.app.usecase.sessionContactV2
import java.io.File

/**
 * @author liuzhongao
 * @since 2024/6/18 23:41
 */

actual fun imInitConfig(): IMInitConfig {
    return IMInitConfig(
        remoteAddress = "",
        token = Token(sessionContactV2.sessionId, sessionContactV2.sessionId),
        factory = { sessionId ->
            AppMessageDatabase(
                context = BasicApplication.applicationContext,
                sessionId = sessionId
            )
        }
    )
}
