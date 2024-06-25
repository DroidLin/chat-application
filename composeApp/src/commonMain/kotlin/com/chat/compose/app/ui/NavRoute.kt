package com.chat.compose.app.ui

import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/16 21:49
 */
interface NavRoute {

    val route: String

    object Login : NavRoute {
        override val route: String = "route/login"
    }

    object RegisterAccount : NavRoute {
        override val route: String = "route/register/account"
    }

    object ChatSessionList : NavRoute {
        override val route: String get() = "homeRoute"
    }

    object ChatMessageDetail : NavRoute {

        override val route: String get() = "chatMessageDetail/{sessionId}/{sessionType}"

        fun buildRoute(sessionId: String, sessionType: SessionType): String {
            return "chatMessageDetail/$sessionId/${sessionType.value}"
        }
    }

    object Settings : NavRoute {
        override val route: String get() = "settings"
    }

}