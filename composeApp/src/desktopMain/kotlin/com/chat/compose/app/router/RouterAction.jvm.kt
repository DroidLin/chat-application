package com.chat.compose.app.router

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
actual fun rememberRouterAction(): RouterAction {
    val navigator = rememberNavController()
    return remember(navigator) { RouterActionImpl(navigator) }
}


@Stable
private class RouterActionImpl(
    override val navController: NavHostController,
) : RouterAction {

    override fun navigateTo(route: String) {
        this.navController.navigate(route)
    }

    override fun navigateTo(route: String, navOptions: NavOptions?) {
        this.navController.navigate(route, navOptions)
    }

    override fun backPress() {
        this.navController.popBackStack()
    }
}