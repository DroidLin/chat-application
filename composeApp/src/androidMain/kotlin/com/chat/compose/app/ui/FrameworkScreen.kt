package com.chat.compose.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.application.channel.message.SessionType
import com.chat.compose.app.router.rememberRouterAction
import com.chat.compose.app.screen.message.ui.SessionDetailScreen
import com.chat.compose.app.screen.message.ui.SessionListScreen
import com.chat.compose.app.screen.setting.SettingScreen

/**
 * @author liuzhongao
 * @since 2024/6/20 23:57
 */
@Composable
fun FrameworkScreen() {
    val routerAction = rememberRouterAction()
    val navigateTo = { route: String ->
        routerAction.navigateTo(
            route = route,
            navOptions = navOptions {
                launchSingleTop = true
                popUpTo(route) {
                    inclusive = true
                }
            }
        )
    }
    Box {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = routerAction.navController,
            startDestination = NavRoute.ChatSessionList.route,
        ) {
            composable(
                route = NavRoute.ChatSessionList.route,
            ) {
                Surface {
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
            }
            composable(
                route = NavRoute.ChatMessageDetail.route
            ) { backStackEntry ->
                Surface {
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
            }
            composable(
                route = NavRoute.Settings.route,
            ) {
                Surface {
                    SettingScreen(
                        backPressed = routerAction::backPress
                    )
                }
            }
        }
        val currentEntry = routerAction.navController.currentBackStackEntryAsState()
        val currentRoute by remember { derivedStateOf { currentEntry.value?.destination?.route } }
        val showNavigationBar = remember {
            derivedStateOf {
                currentRoute == NavRoute.ChatSessionList.route || currentRoute == NavRoute.Settings.route
            }
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = showNavigationBar.value,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == NavRoute.ChatSessionList.route,
                    onClick = { navigateTo(NavRoute.ChatSessionList.route) },
                    icon = {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null)
                    },
                    label = { Text("Chat") },
                )
                NavigationBarItem(
                    selected = currentRoute == NavRoute.Settings.route,
                    onClick = { navigateTo(NavRoute.Settings.route) },
                    icon = {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    },
                    label = { Text("Settings") },
                )
            }
        }
    }
}