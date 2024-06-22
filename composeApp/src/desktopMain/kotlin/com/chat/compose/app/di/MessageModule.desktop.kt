package com.chat.compose.app.di

import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.jvm.AppMessageDatabase
import com.application.channel.im.IMInitConfig
import com.application.channel.im.Token
import com.chat.compose.app.usecase.sessionContactV1
import java.io.File

/**
 * @author liuzhongao
 * @since 2024/6/18 23:43
 */

private val databaseFactory = AppMessageDatabase.Factory { sessionId ->
    val filePath = File("database${File.separator}message-database-${sessionId}.db").absolutePath
    AppMessageDatabase(
        filePath = filePath,
        sessionId = sessionId
    )
}

actual fun imInitConfig(): IMInitConfig {
    return IMInitConfig(
        remoteAddress = "http://192.168.2.110:8325",
        token = Token(sessionContactV1.sessionId, sessionContactV1.sessionId),
        factory = databaseFactory
    )
}