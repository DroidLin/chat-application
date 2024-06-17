package com.chat.compose.app.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.application.channel.message.SessionType
import com.chat.compose.app.router.rememberRouterAction
import com.chat.compose.app.screen.message.ui.SessionDetailScreen
import com.chat.compose.app.screen.message.ui.SessionListScreen
import com.chat.compose.app.screen.setting.SettingScreen
import com.chat.compose.app.ui.NavRoute
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.*
import moe.tlaster.precompose.navigation.transition.NavTransition

private val homeNavigationScreen = NavTransition(
    createTransition = fadeIn(animationSpec = tween(300)),
    destroyTransition = fadeOut(animationSpec = tween(300)),
    resumeTransition = fadeIn(animationSpec = tween(300)),
    pauseTransition = fadeOut(animationSpec = tween(300))
)
private val otherNavigationTransition = NavTransition(
    createTransition = slideInHorizontally(animationSpec = tween(300)) { it },
    destroyTransition = slideOutHorizontally(animationSpec = tween(300)) { it },
    resumeTransition = fadeIn(animationSpec = tween(300)),
    pauseTransition = fadeOut(animationSpec = tween(300))
)

@Composable
fun BasicApplication() {
    val routerAction = rememberRouterAction()

    val navigateTo = remember(routerAction.navigator) {
        { route: String ->
            routerAction.navigateTo(
                route = route,
                navOptions = NavOptions(
                    launchSingleTop = true,
                    popUpTo = PopUpTo(
                        route = route,
                        inclusive = true
                    )
                )
            )
        }
    }
    PermanentNavigationDrawer(
        modifier = Modifier,
        drawerContent = {
            val currentEntry = routerAction.navigator.currentEntry.collectAsStateWithLifecycle(null)
            val currentRoute by remember { derivedStateOf { currentEntry.value?.route?.route } }
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
            }
        }
    ) {
        val navigator = rememberNavigator(name = "")
        NavHost(
            navigator = navigator,
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
                SessionListScreen(
                    backPress = routerAction::backPress
                )
            }
            scene(
                route = NavRoute.ChatMessageDetail.route
            ) { backStackEntry ->
                val sessionId: String = requireNotNull(backStackEntry.query("sessionId"))
                val sessionType: SessionType = requireNotNull(backStackEntry.query("sessionType"))
                SessionDetailScreen(
                    sessionId = sessionId,
                    sessionType = sessionType,
                )
            }
            scene(
                route = NavRoute.Settings.route,
                navTransition = homeNavigationScreen
            ) {
                SettingScreen(
                    backPressed = routerAction::backPress
                )
            }
        }
    }

}