package com.chat.compose.app.util

import com.chat.compose.app.storage.MutableMapStorage

/**
 * @author liuzhongao
 * @since 2024/7/1 00:05
 */
object PreferenceCenter {

    val imPreference = lazy { MutableMapStorage(KEY_IM_PREFERENCE) }
    val cookieStorage = lazy { MutableMapStorage(KEY_COOKIE) }
    val loginPreference = lazy { MutableMapStorage(KEY_LOGIN_PREFERENCE) }
    val userBasicPreference get() = lazy { MutableMapStorage(KEY_LOGIN_PREFERENCE) }

    private const val KEY_IM_PREFERENCE = "imPreferences"
    private const val KEY_COOKIE = "cookieStorage"
    private const val KEY_LOGIN_PREFERENCE = "loginPreference"
}