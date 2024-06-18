package com.chat.compose.app.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.application.channel.database.AppMessageDatabase
import com.application.channel.im.IMInitConfig
import com.application.channel.im.Token
import com.chat.compose.app.LocalApplicationConfiguration

/**
 * @author liuzhongao
 * @since 2024/6/18 01:13
 */

@Composable
actual fun viewModelScopeComponent(): ViewModelScopeComponent {
    val initConfig = requireNotNull(LocalApplicationConfiguration.current).initConfig
    return remember(initConfig) {
        DaggerViewModelScopeComponent
            .builder()
            .messageModule(MessageModule(initConfig))
            .build()
    }
}
