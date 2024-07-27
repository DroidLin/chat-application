package com.chat.compose.app.screen.user

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.chat.compose.app.route.RouteAction
import com.chat.compose.app.screen.placeholder.emptyPlaceholder
import com.chat.compose.app.screen.setting.settingScreen
import com.chat.compose.app.ui.DETAIL_HOST_ROUTE
import com.chat.compose.app.ui.ListDetailBizScaffold
import com.chat.compose.app.ui.NavRoute

/**
 * @author liuzhongao
 * @since 2024/7/21 22:14
 */
@Composable
fun PersonalInfoHomeScreen(
    onConfirmLogout: () -> Unit = {}
) {
    ListDetailBizScaffold(
        listPanel = { navigateTo: (buildRoute: () -> String) -> Unit ->
            PersonalInformationScreen(
                navigateToSetting = {
                    navigateTo { NavRoute.Settings.route }
                },
                onConfirmLogout = onConfirmLogout
            )
        },
        detailPanel = { startDestination: String, navigateBack: () -> Unit, routeAction: RouteAction ->
            Surface {
                NavHost(
                    navController = routeAction.navController,
                    startDestination = startDestination,
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                    popEnterTransition = { fadeIn() },
                    popExitTransition = { fadeOut() },
                    route = DETAIL_HOST_ROUTE
                ) {
                    emptyPlaceholder()
                    settingScreen(
                        backPressed = navigateBack
                    )
                }
            }
        }
    )
}