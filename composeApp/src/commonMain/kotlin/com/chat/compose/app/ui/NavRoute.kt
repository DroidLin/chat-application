package com.chat.compose.app.ui

import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/16 21:49
 */
sealed class NavRoute(val route: String, val deepLinks: List<String> = emptyList()) {

    data object SplashScreen : NavRoute(route = "splashScreen")

    data object LoginRoute : NavRoute(route = "login") {
        data object Login : NavRoute(route = "loginScreen")
        data object RegisterAccount : NavRoute(route = "registerAccount")
    }

    data object HomeScreen : NavRoute(route = "homeScreen")

    data object ChatSessionList : NavRoute(route = "homeRoute")
    data object ChatMessageDetail : NavRoute(
        route = "chatDetail/{sessionId}/{sessionType}",
        deepLinks = listOf("chatter://chatDetail?sessionId={sessionId}&sessionType={sessionType}"),
    ) {
        fun buildRoute(sessionId: String, sessionType: SessionType): String {
            return "chatDetail/$sessionId/${sessionType.value}"
        }
    }

    data object Settings : NavRoute(route = "settings")
    data object SearchLauncher : NavRoute(route = "searchLauncher")

    data object SearchComplexResult : NavRoute(
        route = "searchResultComplex?keyword={keyword}",
        deepLinks = listOf("chatter://searchResultComplex?keyword={keyword}")
    ) {
        fun buildRoute(keyword: String): String = "searchResultComplex?keyword=${keyword}"
    }

    data object UserBasicInfo : NavRoute(
        route = "userBasicInfo?userId={userId}",
        deepLinks = listOf("chatter://userBasicInfo?userId={userId}")
    ) {
        fun buildRoute(userId: Long): String = "userBasicInfo?userId=${userId}"
    }

    data object PersonalInfo : NavRoute(route = "personalInfo")

}