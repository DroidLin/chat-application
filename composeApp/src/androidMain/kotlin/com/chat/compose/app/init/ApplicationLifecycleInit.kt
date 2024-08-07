package com.chat.compose.app.init

import android.content.Context
import androidx.lifecycle.*
import com.application.channel.core.util.koinInject
import com.chat.compose.app.AndroidChatService
import com.chat.compose.app.initChatService
import com.chat.compose.app.lifecycle.ApplicationLifecycleObserver
import com.chat.compose.app.lifecycle.ApplicationLifecycleRegistry
import com.chat.compose.app.metadata.Profile
import com.chat.compose.app.services.ProfileService
import com.chat.compose.app.util.PreferenceCenter

/**
 * @author liuzhongao
 * @since 2024/7/14 10:59
 */

fun initApplicationLifecycleObserver(applicationContext: Context) {
    val observer = object : ApplicationLifecycleObserver {
        override suspend fun onUserLogin(profile: Profile) {
            initChatService()
            AndroidChatService.start(applicationContext)
        }

        override suspend fun onUserLogout() {
            AndroidChatService.stop(applicationContext)
            PreferenceCenter.logout()
            koinInject<ProfileService>().logout()
        }

        override suspend fun onFirstFrameComplete() {
            initChatService()
            AndroidChatService.start(applicationContext)
        }
    }
    ApplicationLifecycleRegistry.addObserver(observer)
}

fun initApplicationProcessLifecycle() {
    val observer = object : DefaultLifecycleObserver {

    }
    ProcessLifecycleOwner.get().lifecycle
}