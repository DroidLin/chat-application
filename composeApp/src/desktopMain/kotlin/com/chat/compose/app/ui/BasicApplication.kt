package com.chat.compose.app.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.application.channel.core.util.koinInject
import com.application.channel.message.SessionType
import com.chat.compose.app.LocalApplicationConfiguration
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
import com.chat.compose.app.ui.framework.Box

@Composable
fun BasicApplication() {
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
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize()
                    .navigationRailPadding(),
                navController = routerAction.navController,
                startDestination = NavRoute.SplashScreen.route,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() }
            ) {
                homeNavigationComposable(
                    route = NavRoute.SplashScreen.route
                ) {
                    Surface {
                        SplashScreen(
                            onInitialFinished = {
                                val profileService = koinInject<ProfileService>()
                                if (profileService.profile.isValid) {
                                    navigateTo(NavRoute.ChatSessionList.route)
                                } else navigateTo(NavRoute.LoginRoute.route)
                            }
                        )
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
                composable(
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
                composable(
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
                    val sessionId: String =
                        requireNotNull(backStackEntry.arguments?.getString("sessionId"))
                    val sessionType: SessionType = SessionType.fromValue(
                        requireNotNull(
                            backStackEntry.arguments?.getString("sessionType")?.toIntOrNull()
                        )
                    )
                    Surface {
                        SessionDetailScreen(
                            sessionId = sessionId,
                            sessionType = sessionType,
                            backPress = routerAction::backPress
                        )
                    }
                }
                composable(
                    route = NavRoute.SearchLauncher.route
                ) {
                    SearchLauncherScreen(
                        backPressed = routerAction::backPress
                    )
                }
            }
            AppNavigationRail(modifier = Modifier.align(Alignment.CenterStart), navigateTo)
        }
    }
}

@Composable
private fun AppNavigationRail(
    modifier: Modifier = Modifier,
    switchHomeTo: (String) -> Unit
) {
    val applicationConfiguration = LocalApplicationConfiguration.current
    val routerAction = LocalRouterAction.current
    Box(
        modifier = modifier,
    ) {
        val currentEntry = routerAction.navController.currentBackStackEntryAsState()
        val currentRoute by remember { derivedStateOf { currentEntry.value?.destination?.route } }
        val navigationRailVisible by remember {
            derivedStateOf {
                currentRoute != null
                        && currentRoute != NavRoute.SplashScreen.route
                        && currentRoute != NavRoute.LoginRoute.route
                        && currentRoute != NavRoute.LoginRoute.Login.route
                        && currentRoute != NavRoute.LoginRoute.RegisterAccount.route
            }
        }
        AnimatedVisibility(
            visible = navigationRailVisible,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it }
        ) {
            NavigationRail(
                modifier = Modifier
                    .fillMaxHeight()
                    .applyNavigationRailSize(),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = "Basic")
                Spacer(modifier = Modifier.height(8.dp))
                NavigationRailItem(
                    selected = currentRoute == NavRoute.ChatSessionList.route,
                    onClick = {
                        switchHomeTo(NavRoute.ChatSessionList.route)
                    },
                    icon = {
                        Icon(imageVector = Icons.Filled.Star, contentDescription = null)
                    }
                )
                NavigationRailItem(
                    selected = currentRoute == NavRoute.Settings.route,
                    onClick = {
                        switchHomeTo(NavRoute.Settings.route)
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
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}