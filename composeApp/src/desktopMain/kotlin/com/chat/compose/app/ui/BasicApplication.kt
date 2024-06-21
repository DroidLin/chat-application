package com.chat.compose.app.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.application.channel.message.SessionType
import com.chat.compose.app.LocalApplicationConfiguration
import com.chat.compose.app.router.rememberRouterAction
import com.chat.compose.app.screen.message.ui.SessionDetailScreen
import com.chat.compose.app.screen.message.ui.SessionListScreen
import com.chat.compose.app.screen.setting.SettingScreen

@Composable
fun BasicApplication() {
    val routerAction = rememberRouterAction()

    val navigateTo = remember(routerAction.navController) {
        { route: String ->
            routerAction.navigateTo(
                route = route,
                navOptions = navOptions {
                    launchSingleTop = true
                    popUpTo(route = "") {
                        inclusive = true
                    }
                }
            )
        }
    }
    val applicationConfiguration = LocalApplicationConfiguration.current
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        val currentEntry = routerAction.navController.currentBackStackEntryAsState()
        val currentRoute by remember { derivedStateOf { currentEntry.value?.destination?.route } }
        NavigationRail(
            modifier = Modifier,
            header = {
                Text(text = "Basic")
            }
        ) {
            NavigationRailItem(
                selected = currentRoute == NavRoute.ChatSessionList.route,
                onClick = {
                    navigateTo(NavRoute.ChatSessionList.route)
                },
                icon = {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = null)
                }
            )
            NavigationRailItem(
                selected = currentRoute == NavRoute.Settings.route,
                onClick = {
                    navigateTo(NavRoute.Settings.route)
                },
                icon = {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = null)
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            NavigationRailItem(
                modifier = Modifier,
                selected = false,
                onClick = {
                    applicationConfiguration.isDarkMode = !applicationConfiguration.isDarkMode
                },
                icon = {
                    Icon(imageVector = Icons.Filled.Phone, contentDescription = null)
                }
            )
        }
        VerticalDivider()
        NavHost(
            navController = routerAction.navController,
            startDestination = NavRoute.ChatSessionList.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(
                route = NavRoute.ChatSessionList.route,
            ) {
                SessionListScreen(
                    backPress = routerAction::backPress,
                    sessionItemClick = { sessionContact ->
                        val route = NavRoute.ChatMessageDetail.buildRoute(
                            sessionId = sessionContact.sessionId,
                            sessionType = sessionContact.sessionType,
                        )
                        routerAction.navigateTo(route)
                    }
                )
            }
            navigationComposable(
                route = NavRoute.ChatMessageDetail.route
            ) { backStackEntry ->
                val sessionId: String =
                    requireNotNull(backStackEntry.arguments?.getString("sessionId"))
                val sessionType: SessionType = SessionType.fromValue(
                    requireNotNull(
                        backStackEntry.arguments?.getString("sessionType")?.toIntOrNull()
                    )
                )
                SessionDetailScreen(
                    sessionId = sessionId,
                    sessionType = sessionType,
                    backPress = routerAction::backPress
                )
            }
            composable(
                route = NavRoute.Settings.route,
            ) {
                SettingScreen(
                    backPressed = routerAction::backPress
                )
            }
        }
    }
}