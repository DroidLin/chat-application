package com.chat.compose.app.util

import com.chat.compose.app.storage.MutableMapStorage

/**
 * @author liuzhongao
 * @since 2024/7/1 00:05
 */
object PreferenceCenter {
    private const val KEY_COOKIE = "cookieStorage"
    private const val KEY_LOGIN_PREFERENCE = "loginPreference"

    val cookieStorage = lazy { MutableMapStorage(KEY_COOKIE) }
    val loginPreference = lazy { MutableMapStorage(KEY_LOGIN_PREFERENCE) }
    val userBasicPreference get() = lazy { MutableMapStorage(KEY_LOGIN_PREFERENCE) }

    fun logout() {
        this.cookieStorage.value.clearAndFlush()
        this.loginPreference.value.clearAndFlush()
        this.userBasicPreference.value.clearAndFlush()
    }
}