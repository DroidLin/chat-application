package com.chat.compose.app.lifecycle

import com.chat.compose.app.metadata.Profile

/**
 * @author liuzhongao
 * @since 2024/6/29 20:46
 */
interface ApplicationLifecycleObserver {

    suspend fun onUserLogin(profile: Profile) {}
    suspend fun onUserLogout() {}

    suspend fun onFirstFrameComplete() {}
}