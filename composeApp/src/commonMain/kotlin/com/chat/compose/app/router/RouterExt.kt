package com.chat.compose.app.router

import androidx.navigation.navOptions
import com.application.channel.core.util.koinInject
import com.application.channel.message.SessionType
import com.chat.compose.app.metadata.UiRecentContact
import com.chat.compose.app.metadata.isValid
import com.chat.compose.app.services.ProfileService
import com.chat.compose.app.ui.NavRoute

/**
 * @author liuzhongao
 * @since 2024/7/20 00:43
 */

fun RouteAction.onInitFinished() {
    val currentRoute = this.navController.currentDestination?.route
    val options = navOptions {
        launchSingleTop = true
        if (currentRoute.isNullOrEmpty()) return@navOptions
        popUpTo(currentRoute) { inclusive = true }
    }
    val profileService = koinInject<ProfileService>()
    if (profileService.profile.isValid) {
        this.navigateTo(NavRoute.HomeScreen.route, options)
    } else this.navigateTo(NavRoute.LoginRoute.route, options)
}

fun RouteAction.navigationBarRoute(navRoute: NavRoute) {
    val currentRoute = this.navController.currentDestination?.route
    val options = navOptions {
        launchSingleTop = true
        if (currentRoute.isNullOrEmpty()) return@navOptions
        popUpTo(currentRoute) { inclusive = true }
    }
    this.navigateTo(navRoute.route, options)
}

fun RouteAction.navigateWhenLoginComplete() {
    this.navigateTo(NavRoute.HomeScreen.route)
}

fun RouteAction.navigateToChatDetail(sessionContact: UiRecentContact) {
    this.navigateToChatDetail(sessionContact.sessionId, sessionContact.sessionType)
}

fun RouteAction.navigateToChatDetail(sessionId: String, sessionType: SessionType) {
    val route = NavRoute.ChatMessageDetail.buildRoute(
        sessionId = sessionId,
        sessionType = sessionType,
    )
    this.navigateTo(
        route = route,
        navOptions = navOptions {
            launchSingleTop = true
            popUpTo(NavRoute.ChatMessageDetail.route) { inclusive = true }
        }
    )
}

fun RouteAction.navigateToSearchLauncher() {
    this.navigateTo(NavRoute.SearchLauncher.route)
}

fun RouteAction.navigateToSearchResult(keyword: String) {
    val route = NavRoute.SearchComplexResult.buildRoute(keyword)
    this.navigateTo(route = route)
}

fun RouteAction.navigateToSettings() {
    this.navigateTo(NavRoute.Settings.route)
}

fun RouteAction.navigateToUserBasicInfo(userId: Long) {
    val route = NavRoute.UserBasicInfo.buildRoute(userId = userId)
    this.navigateTo(route = route)
}