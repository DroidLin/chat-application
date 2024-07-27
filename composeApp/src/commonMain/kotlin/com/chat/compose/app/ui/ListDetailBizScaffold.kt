package com.chat.compose.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.*
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.navOptions
import com.chat.compose.app.lifecycle.MainFirstFrameContent
import com.chat.compose.app.platform.ui.LocalAppConfig
import com.chat.compose.app.platform.ui.LocalWindowAdaptiveInfo
import com.chat.compose.app.route.RouteAction
import com.chat.compose.app.route.RouteActionProvider
import com.chat.compose.app.route.rememberRouteAction
import com.chat.compose.app.screen.message.ui.*
import java.util.*

/**
 * @author liuzhongao
 * @since 2024/7/27 09:20
 */
const val DETAIL_HOST_ROUTE = "detail_host_route"

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListDetailBizScaffold(
    listPanel: @Composable (
        navigateTo: (
            buildRoute: () -> String
        ) -> Unit
    ) -> Unit,
    detailPanel: @Composable (
        startDestination: String,
        navigateBack: () -> Unit,
        routeAction: RouteAction,
    ) -> Unit,
) {
    val appConfig = LocalAppConfig.current
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>(
        scaffoldDirective = calculatePaneScaffoldDirective(
            windowAdaptiveInfo = LocalWindowAdaptiveInfo.current
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
        rememberRouteAction()
    }

    val isListVisible = remember {
        derivedStateOf {
            navigator.isListVisible
        }
    }
    appConfig.showNavigationBar = isListVisible.value

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
                listPanel(::navigateTo)
            }
        },
        detailPane = {
            AnimatedPane(
                modifier = Modifier.fillMaxSize()
            ) {
                routeAction.RouteActionProvider {
                    key(nestedNavKey) {
                        detailPanel(startDestinationRoute, ::navigateBack, routeAction)
                    }
                }
            }
        }
    )
}