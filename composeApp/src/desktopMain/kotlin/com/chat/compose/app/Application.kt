package com.chat.compose.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.android.dependencies.common.ComponentFacade
import com.application.channel.core.client.TcpClientModule
import com.application.channel.core.util.koinInject
import com.application.channel.im.IMDatabaseInitConfig
import com.application.channel.im.IMInitConfig
import com.application.channel.im.MsgConnectionService
import com.application.channel.im.SingleIMManager
import com.application.channel.message.ChatServiceModule
import com.chat.compose.app.di.messageModule
import com.chat.compose.app.di.useCaseModule
import com.chat.compose.app.di.viewModelModule
import com.chat.compose.app.metadata.ApplicationConfiguration
import com.chat.compose.app.ui.BasicApplication
import com.chat.compose.app.ui.DesktopMaterialTheme
import org.koin.core.context.startKoin

/**
 * @author liuzhongao
 * @since 2024/6/16 10:36
 */
fun main() {
    ComponentFacade
    startKoin {
        modules(
            viewModelModule,
            messageModule,
            useCaseModule,
            TcpClientModule,
            ChatServiceModule
        )
    }
    initAndStartChatService()
    application {
        val isSystemInDarkMode = isSystemInDarkTheme()
        val applicationConfiguration = remember(isSystemInDarkMode) {
            ApplicationConfiguration(isDarkMode = isSystemInDarkMode)
        }
        CompositionLocalProvider(
            LocalApplicationConfiguration provides applicationConfiguration
        ) {
            Window(
                onCloseRequest = ::exitApplication,
                title = applicationConfiguration.title,
                undecorated = false,
                transparent = false,
                state = rememberWindowState(
                    placement = WindowPlacement.Floating,
                    position = WindowPosition.Aligned(Alignment.Center),
                    size = DpSize(1000.dp, 800.dp)
                )
            ) {
                CompositionLocalProvider(LocalWindow provides this.window) {
                    DesktopMaterialTheme(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        BasicApplication()
                    }
                }
            }
        }
    }
}

val LocalWindow = compositionLocalOf<ComposeWindow> { error("not provided") }
val LocalApplicationConfiguration = compositionLocalOf<ApplicationConfiguration> { error("not provided") }