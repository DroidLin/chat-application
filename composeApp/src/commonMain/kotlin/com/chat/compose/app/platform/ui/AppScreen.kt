package com.chat.compose.app.platform.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.lifecycle.ApplicationLifecycleRegistry
import com.chat.compose.app.screen.framework.AppHomeEnum
import com.chat.compose.app.screen.framework.AppViewModel
import com.chat.compose.app.screen.message.ui.MessageHomeScreen
import com.chat.compose.app.screen.user.PersonalInfoHomeScreen
import com.chat.compose.app.ui.NavigationScaffold
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/7/26 00:29
 */

val LocalAppConfig = compositionLocalOf<AppConfig> { error("not provided") }

class AppConfig {
    var showNavigationBar: Boolean by mutableStateOf(true)
}

@Composable
fun AppConfigProvider(content: @Composable () -> Unit) {
    val appConfig = remember { AppConfig() }
    CompositionLocalProvider(LocalAppConfig provides appConfig, content = content)
}

@Composable
fun AppScreen(modifier: Modifier) {
    val viewModel = koinViewModel<AppViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val homeEnum by remember {
        derivedStateOf {
            uiState.value.homeEnum
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val confirmLogout: () -> Unit = {
        coroutineScope.launch {
            ApplicationLifecycleRegistry.onUserLogout()
        }
    }
    AppConfigProvider {
        val appConfig = LocalAppConfig.current
        NavigationScaffold(
            modifier = modifier,
            showNavigation = appConfig.showNavigationBar,
            navigationContent = {
                if (this is RowScope) {
                    AppHomeEnum.entries.forEach { enum ->
                        NavigationBarItem(
                            selected = homeEnum == enum,
                            onClick = { viewModel.switchNavigationTo(enum) },
                            icon = { Icon(imageVector = enum.icon, contentDescription = null) },
                            label = { Text(text = stringResource(enum.title)) },
                        )
                    }
                } else if (this is ColumnScope) {
                    AppHomeEnum.entries.forEach { enum ->
                        NavigationRailItem(
                            selected = homeEnum == enum,
                            onClick = { viewModel.switchNavigationTo(enum) },
                            icon = { Icon(imageVector = enum.icon, contentDescription = null) },
                            label = { Text(text = stringResource(enum.title)) }
                        )
                    }
                }
            }
        ) {
            AnimatedContent(
                targetState = homeEnum,
                contentAlignment = Alignment.Center,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                contentKey = { it }
            ) {
                when (it) {
                    AppHomeEnum.Message -> MessageHomeScreen(confirmLogout)
                    AppHomeEnum.Personal -> PersonalInfoHomeScreen(confirmLogout)
                    else -> {}
                }
            }
        }
    }
}