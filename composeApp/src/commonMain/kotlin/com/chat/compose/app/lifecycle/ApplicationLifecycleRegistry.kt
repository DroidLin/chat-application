package com.chat.compose.app.lifecycle

import com.chat.compose.app.metadata.Profile
import okio.withLock
import java.util.concurrent.locks.ReentrantLock

/**
 * @author liuzhongao
 * @since 2024/6/29 20:45
 */
object ApplicationLifecycleRegistry : ApplicationLifecycleObserver {

    private val lock = ReentrantLock()
    private val observerList = mutableListOf<ApplicationLifecycleObserver>()

    fun addObserver(observer: ApplicationLifecycleObserver) {
        this.update { this.observerList += observer }
    }

    fun removeObserver(observer: ApplicationLifecycleObserver) {
        this.update { this.observerList -= observer }
    }

    override suspend fun onUserLogin(profile: Profile) {
        this.notifySafely { onUserLogin(profile) }
    }

    override suspend fun onUserLogout() {
        this.notifySafely { onUserLogout() }
    }

    override suspend fun onLoginSessionExpired() {
        this.notifySafely { onLoginSessionExpired() }
    }

    override suspend fun onFirstFrameComplete() {
        this.notifySafely { onFirstFrameComplete() }
    }

    private inline fun update(function: () -> Unit) {
        this.lock.withLock(function)
    }

    private inline fun notifySafely(function: ApplicationLifecycleObserver.() -> Unit) {
        this.lock.withLock { this.observerList.forEach(function) }
    }
}