package com.chat.compose.app.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.router.RouteAction
import com.chat.compose.app.router.RouteActionProvider
import com.chat.compose.app.screen.framework.FrameworkHomeEnum
import com.chat.compose.app.screen.framework.FrameworkViewModel
import com.chat.compose.app.screen.message.ui.MessageHomeScreen
import com.chat.compose.app.screen.user.PersonalInfoHomeScreen
import com.chat.compose.app.ui.appSafeAreaPadding
import com.chat.compose.app.ui.applyAppSafeArea
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/6/20 23:57
 */
@Composable
@JvmOverloads
actual fun FrameworkScreen(routeAction: RouteAction) {
    val viewModel = koinViewModel<FrameworkViewModel>()
    val uiState = viewModel.uiState.collectAsState()
    val homeEnum by remember {
        derivedStateOf {
            uiState.value.homeEnum
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .appSafeAreaPadding()
        ) {
            when (homeEnum) {
                FrameworkHomeEnum.Message -> MessageHomeScreen()
                FrameworkHomeEnum.Personal -> PersonalInfoHomeScreen()
                else -> {}
            }
        }
        AnimatedVisibility(
            modifier = Modifier.applyAppSafeArea(),
            visible = false
        ) {
            AppNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                homeEnum = homeEnum,
                switchNavigation = viewModel::switchNavigationTo
            )
        }
    }
}

@Composable
private fun AppNavigationBar(
    modifier: Modifier = Modifier,
    homeEnum: FrameworkHomeEnum,
    switchNavigation: (FrameworkHomeEnum) -> Unit,
) {
    NavigationBar(
        modifier = modifier
    ) {
        FrameworkHomeEnum.entries.forEach { enum ->
            NavigationBarItem(
                selected = homeEnum == enum,
                onClick = { switchNavigation(enum) },
                icon = {
                    Icon(imageVector = enum.icon, contentDescription = null)
                },
                label = { Text(text = stringResource(enum.title)) },
            )
        }
    }
}