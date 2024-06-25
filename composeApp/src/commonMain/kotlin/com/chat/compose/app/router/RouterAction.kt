package com.chat.compose.app.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions

/**
 * @author liuzhongao
 * @since 2024/6/16 21:56
 */
@Stable
interface RouterAction {

    val navController: NavHostController

    fun navigateTo(route: String)

    fun navigateTo(route: String, navOptions: NavOptions?)

    fun backPress()
}

@Composable
expect fun rememberRouterAction(): RouterAction

val LocalRouterAction = compositionLocalOf<RouterAction> { error("No LocalRouterAction provided") }