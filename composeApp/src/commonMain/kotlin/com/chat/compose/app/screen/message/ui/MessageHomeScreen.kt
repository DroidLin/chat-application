package com.chat.compose.app.screen.message.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.*
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.chat.compose.app.lifecycle.MainFirstFrameContent
import com.chat.compose.app.router.*
import com.chat.compose.app.screen.placeholder.emptyPlaceholder
import com.chat.compose.app.screen.search.searchLauncherScreen
import com.chat.compose.app.screen.search.searchResultScreen
import com.chat.compose.app.screen.user.personalInformationScreen
import com.chat.compose.app.screen.user.userBasicInfoScreen
import com.chat.compose.app.ui.AppBackHandler
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.calculateWindowAdaptiveInfo
import java.util.*

/**
 * @author liuzhongao
 * @since 2024/7/21 11:01
 */
private const val DETAIL_HOST_ROUTE = "detail_host_route"

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MessageHomeScreen(
    confirmLogout: () -> Unit = {}
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>(
        scaffoldDirective = calculatePaneScaffoldDirective(
            windowAdaptiveInfo = calculateWindowAdaptiveInfo()
        ),
        initialDestinationHistory = listOf(
            ThreePaneScaffoldDestinationItem(pane = ListDetailPaneScaffoldRole.List)
        )
    )

    var startDestinationRoute by remember { mutableStateOf(NavRoute.EmptyPlaceHolder.route) }
    var nestedNavKey by rememberSaveable(
        stateSaver = Saver(
            save = { it.toString() },
            restore = UUID::fromString
        )
    ) {
        mutableStateOf(UUID.randomUUID())
    }

    val routeAction = key(nestedNavKey) {
        rememberRouterAction()
    }

    fun navigateTo(buildRoute: () -> String) {
        if (navigator.isDetailVisible) {
            val route = buildRoute()
            routeAction.navigateTo(route, navOptions { popUpTo(DETAIL_HOST_ROUTE) })
        } else {
            startDestinationRoute = buildRoute()
            nestedNavKey = UUID.randomUUID()
        }
        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
    }

    fun navigateBack() {
        if (routeAction.backPress()) {
            return
        }
        if (navigator.navigateBack(BackNavigationBehavior.PopLatest)) {
            return
        }
        println("navigate back failure.")
    }

    val canNavigateBack = remember {
        derivedStateOf {
            navigator.canNavigateBack()
        }
    }
    AppBackHandler(canNavigateBack.value, navigator::navigateBack)
    MainFirstFrameContent()
    ListDetailPaneScaffold(
        modifier = Modifier.fillMaxSize(),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane(
                modifier = Modifier.fillMaxSize()
            ) {
                MainFirstFrameContent()
                SessionListScreen(
                    sessionItemClick = { uiSessionContact ->
                        navigateTo {
                            NavRoute.ChatMessageDetail.buildRoute(
                                sessionId = uiSessionContact.sessionId,
                                sessionType = uiSessionContact.sessionType
                            )
                        }
                    },
                    navigateToSearch = {
                        navigateTo { NavRoute.SearchLauncher.route }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane(
                modifier = Modifier.fillMaxSize()
            ) {
                routeAction.RouteActionProvider {
                    key(nestedNavKey) {
                        MessageHomeDetailPane(
                            routeAction = routeAction,
                            startDestinationRoute = startDestinationRoute,
                            navigateBack = ::navigateBack,
                            confirmLogout = confirmLogout,
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun MessageHomeDetailPane(
    routeAction: RouteAction,
    startDestinationRoute: String,
    navigateBack: () -> Unit,
    confirmLogout: () -> Unit,
) {
    Surface {
        NavHost(
            navController = routeAction.navController,
            startDestination = startDestinationRoute,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() },
            route = DETAIL_HOST_ROUTE
        ) {
            emptyPlaceholder()
            chatDetailScreen(
                backPress = navigateBack,
                navigateToUserBasicInfo = routeAction::navigateToUserBasicInfo
            )
            personalInformationScreen(
                navigateToSetting = routeAction::navigateToSettings,
                onConfirmLogout = confirmLogout
            )
            userBasicInfoScreen(
                backPress = navigateBack,
                navigateToChat = routeAction::navigateToChatDetail
            )
            searchLauncherScreen(
                backPressed = navigateBack,
                navigateToSearchResult = routeAction::navigateToSearchResult,
            )
            searchResultScreen(
                backPressed = navigateBack,
                navigateToUseBasicScreen = routeAction::navigateToUserBasicInfo
            )
            sessionListScreen(
                sessionItemClick = routeAction::navigateToChatDetail,
                navigateToSearch = routeAction::navigateToSearchLauncher,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private val <T> ThreePaneScaffoldNavigator<T>.isDetailVisible: Boolean
    get() = this.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private val <T> ThreePaneScaffoldNavigator<T>.isListVisible: Boolean
    get() = this.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded