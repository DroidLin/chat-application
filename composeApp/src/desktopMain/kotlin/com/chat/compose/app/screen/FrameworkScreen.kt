package com.chat.compose.app.screen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
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
import com.chat.compose.app.ui.framework.Box
import org.jetbrains.compose.resources.stringResource

@Composable
@JvmOverloads
actual fun FrameworkScreen(routeAction: RouteAction) {
    routeAction.RouteActionProvider {
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
            AppNavigationRail(
                modifier = Modifier
                    .fillMaxHeight()
                    .applyAppSafeArea()
                    .align(Alignment.CenterStart),
                homeEnum = homeEnum,
                switchNavigation = viewModel::switchNavigationTo
            )
        }
    }
}


@Composable
private fun AppNavigationRail(
    modifier: Modifier = Modifier,
    homeEnum: FrameworkHomeEnum,
    switchNavigation: (FrameworkHomeEnum) -> Unit,
) {
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .applyAppSafeArea(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        FrameworkHomeEnum.entries.forEach { enum ->
            NavigationRailItem(
                selected = homeEnum == enum,
                onClick = {
                    switchNavigation(enum)
                },
                icon = {
                    Icon(imageVector = enum.icon, contentDescription = null)
                },
                label = {
                    Text(text = stringResource(enum.title))
                }
            )
        }
    }
}
