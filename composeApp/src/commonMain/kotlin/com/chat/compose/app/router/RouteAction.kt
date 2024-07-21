package com.chat.compose.app.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions

/**
 * @author liuzhongao
 * @since 2024/6/16 21:56
 */
@Stable
interface RouteAction {

    val navController: NavHostController

    fun navigateTo(route: String)

    fun navigateTo(route: String, navOptions: NavOptions?)

    fun backPress(): Boolean
}

@Composable
expect fun rememberRouterAction(): RouteAction

@Composable
fun RouteAction.RouteActionProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        value = LocalRouteAction provides this,
        content = content
    )
}

val LocalRouteAction = compositionLocalOf<RouteAction> { error("No LocalRouterAction provided") }