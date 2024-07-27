package com.chat.compose.app.platform.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.chat.compose.app.route.RouteActionProvider
import com.chat.compose.app.route.rememberRouteAction
import com.chat.compose.app.screen.login.loginScreen
import com.chat.compose.app.screen.login.registerAccountScreen
import com.chat.compose.app.ui.NavRoute

/**
 * @author liuzhongao
 * @since 2024/7/26 00:29
 */
@Composable
fun LoginLogicScreen(modifier: Modifier) {
    val routeAction = rememberRouteAction()
    routeAction.RouteActionProvider {
        NavHost(
            modifier = modifier,
            navController = routeAction.navController,
            startDestination = NavRoute.LoginRoute.Login.route,
            contentAlignment = Alignment.Center
        ) {
            loginScreen {}
            registerAccountScreen()
        }
    }
}