package com.chat.compose.app.di

import com.android.dependencies.common.ComponentFacade
import com.android.dependencies.common.android.IContext
import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.android.AppMessageDatabase
import com.application.channel.im.IMDatabaseInitConfig
import com.application.channel.im.IMInitConfig
import com.application.channel.message.Account
import com.chat.compose.app.usecase.sessionContactV2

/**
 * @author liuzhongao
 * @since 2024/6/18 23:41
 */

private val databaseFactory = AppMessageDatabase.Factory { sessionId ->
    AppMessageDatabase(
        context = ComponentFacade[IContext::class.java].applicationContext,
        sessionId = sessionId
    )
}

actual fun platformAppDatabaseFactory(): AppMessageDatabase.Factory {
    return databaseFactory
}

private val account = Account(sessionContactV2.sessionId, sessionContactV2.sessionId)

actual fun imInitConfig(): IMInitConfig {
    return IMInitConfig(
        remoteAddress = "http://192.168.2.110:8325",
        account = account,
    )
}

actual fun imDatabaseInitConfig(): IMDatabaseInitConfig {
    return IMDatabaseInitConfig(
        account = account,
        factory = databaseFactory
    )
}