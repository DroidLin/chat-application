package com.chat.compose.app.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.chat.compose.app.LocalActivity
import com.chat.compose.app.ui.util.setLightNavigation
import com.chat.compose.app.ui.util.setLightStatusBar
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.ui.LocalBackDispatcherOwner
import org.koin.compose.KoinContext

/**
 * @author liuzhongao
 * @since 2024/6/20 23:47
 */
@Composable
fun AndroidMaterialTheme(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val (localLight, localDark) = remember { YellowTheme }
    val isDarkMode = isSystemInDarkTheme()
    val colorScheme = if (isDarkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicDarkColorScheme(context)
        } else localDark
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicLightColorScheme(context)
        } else localLight
    }

    LaunchedEffect(isDarkMode) {
        activity.window.setLightStatusBar(light = !isDarkMode)
        activity.window.setLightNavigation(light = !isDarkMode)
    }

    MaterialTheme(colorScheme = colorScheme) {
        PreComposeApp {
            KoinContext {
                Surface(modifier = modifier, content = content)
            }
        }
    }
}