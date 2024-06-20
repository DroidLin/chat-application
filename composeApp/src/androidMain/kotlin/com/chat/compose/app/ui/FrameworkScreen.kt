package com.chat.compose.app.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.application.channel.message.SessionType
import com.chat.compose.app.router.rememberRouterAction
import com.chat.compose.app.screen.message.ui.SessionDetailScreen
import com.chat.compose.app.screen.message.ui.SessionListScreen
import com.chat.compose.app.screen.setting.SettingScreen
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.*
import moe.tlaster.precompose.navigation.transition.NavTransition

private const val AnimationDuration = 400
private const val FadeAnimationDuration = 300

private val homeNavigationScreen = NavTransition(
    createTransition = fadeIn(animationSpec = tween(FadeAnimationDuration)),
    pauseTransition = fadeOut(animationSpec = tween(FadeAnimationDuration)),
    destroyTransition = fadeOut(animationSpec = tween(FadeAnimationDuration)),
    resumeTransition = fadeIn(animationSpec = tween(FadeAnimationDuration))
)
private val otherNavigationTransition = NavTransition(
    createTransition = slideInHorizontally(animationSpec = tween(AnimationDuration)) { it },
    pauseTransition = fadeOut(animationSpec = tween(AnimationDuration)),
    destroyTransition = slideOutHorizontally(animationSpec = tween(AnimationDuration)) { it },
    resumeTransition = fadeIn(animationSpec = tween(AnimationDuration)),
    enterTargetContentZIndex = 1f,
    exitTargetContentZIndex = 1f
)

/**
 * @author liuzhongao
 * @since 2024/6/20 23:57
 */
@Composable
fun FrameworkScreen() {
    val routerAction = rememberRouterAction()
    val canGoBack by routerAction.canGoBackState.collectAsStateWithLifecycle()
    val navigateTo = { route: String ->
        val popUpTo = PopUpTo(
            route = "",
            inclusive = true
        )
        val navOptions = NavOptions(
            launchSingleTop = true,
            includePath = true,
            popUpTo = popUpTo
        )
        routerAction.navigateTo(
            route = route,
            navOptions = navOptions
        )
    }
    Box {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navigator = routerAction.navigator,
            initialRoute = NavRoute.ChatSessionList.route,
            swipeProperties = remember {
                SwipeProperties()
            },
            navTransition = otherNavigationTransition
        ) {
            scene(
                route = NavRoute.ChatSessionList.route,
                navTransition = homeNavigationScreen
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
            scene(
                route = NavRoute.ChatMessageDetail.route
            ) { backStackEntry ->
                Surface {
                    val sessionId: String = requireNotNull(backStackEntry.path("sessionId"))
                    val sessionType: SessionType =
                        SessionType.fromValue(requireNotNull(backStackEntry.path("sessionType")))
                    SessionDetailScreen(
                        sessionId = sessionId,
                        sessionType = sessionType,
                        backPress = routerAction::backPress
                    )
                }
            }
            scene(
                route = NavRoute.Settings.route,
                navTransition = homeNavigationScreen
            ) {
                Surface {
                    SettingScreen(
                        backPressed = routerAction::backPress
                    )
                }
            }
        }
        val currentEntry = routerAction.navigator.currentEntry.collectAsStateWithLifecycle(null)
        val currentRoute by remember { derivedStateOf { currentEntry.value?.route?.route } }
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
    BackHandler(canGoBack) {
        routerAction.backPress()
    }
}