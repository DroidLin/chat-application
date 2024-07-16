package com.chat.compose.app.screen

import com.application.channel.message.SessionType
import java.io.Serial
import java.io.Serializable

/**
 * @author liuzhongao
 * @since 2024/7/16 00:06
 */
sealed interface NavigatorMetadata : Serializable

sealed interface ListMetadata : NavigatorMetadata {

    data object SessionList : ListMetadata {
        private fun readResolve(): Any = SessionList
        private const val serialVersionUID: Long = -1051923603311422085L
    }
}

sealed interface DetailMetadata : NavigatorMetadata {

    data class SessionDetailMetadata(val sessionId: String, val sessionType: SessionType) : DetailMetadata {
        companion object {
            private const val serialVersionUID: Long = -4717423642188817554L
        }
    }
}

sealed interface ThirdPartyMetadata : NavigatorMetadata {

    data object SearchLauncher : ThirdPartyMetadata {
        private fun readResolve(): Any = SearchLauncher
        private const val serialVersionUID: Long = 6884413837828730556L
    }

    data class SearchResult(val keyword: String) : ThirdPartyMetadata {
        companion object {
            private const val serialVersionUID: Long = 6195386043274886927L
        }
    }

    data class UserBasicInfoMeta(val userId: Long): ThirdPartyMetadata {
        companion object {
            private const val serialVersionUID: Long = -7882616539601700425L
        }
    }
}
