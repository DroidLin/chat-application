package com.chat.compose.app.screen

import androidx.compose.runtime.Composable
import com.chat.compose.app.router.RouteAction
import com.chat.compose.app.router.rememberRouterAction

/**
 * @author liuzhongao
 * @since 2024/7/17 00:17
 */
@Composable
@JvmOverloads
expect fun FrameworkScreen(routeAction: RouteAction = rememberRouterAction())