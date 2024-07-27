package com.chat.compose.app.screen.message.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.chat.compose.app.route.*
import com.chat.compose.app.screen.placeholder.emptyPlaceholder
import com.chat.compose.app.screen.search.searchLauncherScreen
import com.chat.compose.app.screen.search.searchResultScreen
import com.chat.compose.app.screen.user.personalInformationScreen
import com.chat.compose.app.screen.user.userBasicInfoScreen
import com.chat.compose.app.ui.DETAIL_HOST_ROUTE
import com.chat.compose.app.ui.ListDetailBizScaffold
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.appSafeAreaPadding

/**
 * @author liuzhongao
 * @since 2024/7/21 11:01
 */
@Composable
fun MessageHomeScreen(
    confirmLogout: () -> Unit = {}
) {
    ListDetailBizScaffold(
        listPanel = { navigateTo: (buildRoute: () -> String) -> Unit ->
            SessionListScreen(
                modifier = Modifier
                    .appSafeAreaPadding(),
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
        },
        detailPanel = { startDestination: String, navigateBack: () -> Unit, routeAction: RouteAction ->
            MessageHomeDetailPane(
                routeAction = routeAction,
                startDestinationRoute = startDestination,
                navigateBack = navigateBack,
                confirmLogout = confirmLogout,
            )
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
inline val <T> ThreePaneScaffoldNavigator<T>.isDetailVisible: Boolean
    get() = this.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
inline val <T> ThreePaneScaffoldNavigator<T>.isListVisible: Boolean
    get() = this.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded