package com.chat.compose.app.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController

/**
 * @author liuzhongao
 * @since 2024/6/21 11:24
 */
@Composable
actual fun rememberRouteAction(): RouteAction {
    val navigator = rememberNavController()
    return remember(navigator) { RouteActionImpl(navigator) }
}


@Stable
private class RouteActionImpl(
    override val navController: NavHostController,
) : RouteAction {

    override fun navigateTo(route: String) {
        this.navController.navigate(route)
    }

    override fun navigateTo(route: String, navOptions: NavOptions?) {
        this.navController.navigate(route, navOptions)
    }

    override fun backPress(): Boolean {
        return this.navController.popBackStack()
    }
}