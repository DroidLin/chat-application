package com.chat.compose.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.application.channel.core.util.koinInject
import com.application.channel.message.SessionType
import com.chat.compose.app.lifecycle.MainFirstFrameContent
import com.chat.compose.app.metadata.isValid
import com.chat.compose.app.router.LocalRouterAction
import com.chat.compose.app.router.rememberRouterAction
import com.chat.compose.app.screen.login.LoginScreen
import com.chat.compose.app.screen.login.RegisterAccountScreen
import com.chat.compose.app.screen.message.ui.SessionDetailScreen
import com.chat.compose.app.screen.message.ui.SessionListScreen
import com.chat.compose.app.screen.search.SearchLauncherScreen
import com.chat.compose.app.screen.setting.SettingScreen
import com.chat.compose.app.screen.splash.SplashScreen
import com.chat.compose.app.services.ProfileService

/**
 * @author liuzhongao
 * @since 2024/6/20 23:57
 */
@Composable
fun FrameworkScreen() {
    val routerAction = rememberRouterAction()
    val navigateTo = { route: String ->
        val currentRoute = routerAction.navController.currentDestination?.route
        routerAction.navigateTo(
            route = route,
            navOptions = navOptions {
                launchSingleTop = true
                if (!currentRoute.isNullOrEmpty()) {
                    popUpTo(currentRoute) {
                        inclusive = true
                    }
                }
            }
        )
    }
    CompositionLocalProvider(LocalRouterAction provides routerAction) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = routerAction.navController,
                startDestination = NavRoute.SplashScreen.route,
            ) {
                homeNavigationComposable(
                    route = NavRoute.SplashScreen.route
                ) {
                    Surface {
                        SplashScreen {
                            val profileService = koinInject<ProfileService>()
                            if (profileService.profile.isValid) {
                                navigateTo(NavRoute.ChatSessionList.route)
                            } else navigateTo(NavRoute.LoginRoute.route)
                        }
                    }
                }
                navigation(
                    startDestination = NavRoute.LoginRoute.Login.route,
                    route = NavRoute.LoginRoute.route,
                ) {
                    homeNavigationComposable(
                        route = NavRoute.LoginRoute.Login.route,
                    ) {
                        Surface {
                            LoginScreen(
                                loginComplete = {
                                    navigateTo(NavRoute.ChatSessionList.route)
                                }
                            )
                        }
                    }
                    homeNavigationComposable(
                        route = NavRoute.LoginRoute.RegisterAccount.route
                    ) {
                        Surface {
                            RegisterAccountScreen()
                        }
                    }
                }
                homeNavigationComposable(
                    route = NavRoute.ChatSessionList.route,
                ) {
                    MainFirstFrameContent()
                    Surface {
                        SessionListScreen(
                            backPress = routerAction::backPress,
                            sessionItemClick = { sessionContact ->
                                val route = NavRoute.ChatMessageDetail.buildRoute(
                                    sessionId = sessionContact.sessionId,
                                    sessionType = sessionContact.sessionType,
                                )
                                routerAction.navigateTo(route)
                            },
                            navigateToSearch = {
                                routerAction.navigateTo(NavRoute.SearchLauncher.route)
                            }
                        )
                    }
                }
                homeNavigationComposable(
                    route = NavRoute.Settings.route,
                ) {
                    Surface {
                        SettingScreen(
                            backPressed = routerAction::backPress
                        )
                    }
                }
                navigationComposable(
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
                navigationComposable(
                    route = NavRoute.SearchLauncher.route
                ) {
                    SearchLauncherScreen(
                        backPressed = routerAction::backPress
                    )
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
}