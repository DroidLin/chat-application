package com.chat.compose.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.chat.compose.app.metadata.Profile

/**
 * @author liuzhongao
 * @since 2024/7/27 11:20
 */
val LocalProfile = staticCompositionLocalOf<Profile> { error("No LocalProfile provided") }

@Composable
fun Profile.ProfileProviderScope(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalProfile provides this, content = content)
}