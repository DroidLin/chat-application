package com.chat.compose.app.ui

import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/16 21:49
 */
interface NavRoute {

    val route: String

    object ChatSessionList : NavRoute {
        override val route: String get() = "homeRoute"
    }

    object ChatMessageDetail : NavRoute {

        override val route: String get() = "chatMessageDetail/{sessionId}/{sessionType}"

        fun buildRoute(sessionId: String, sessionType: SessionType): String {
            return "chatMessageDetail/$sessionId/$sessionType"
        }
    }

    object Settings : NavRoute {
        override val route: String get() = "settings"
    }

}