package com.chat.compose.app.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.chat.compose.app.router.rememberRouterAction
import com.chat.compose.app.screen.login.LoginScreen

/**
 * @author liuzhongao
 * @since 2024/6/24 23:56
 */
@Composable
fun LoginApplication() {
    val routerAction = rememberRouterAction()
    NavHost(
        navController = routerAction.navController,
        startDestination = NavRoute.Login.route,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        homeNavigationComposable(
            route = NavRoute.Login.route,
        ) {
            LoginScreen()
        }
    }
}