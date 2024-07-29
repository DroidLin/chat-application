package com.chat.compose.app.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

/**
 * @author liuzhongao
 * @since 2024/7/30 00:11
 */

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope> { error("No SharedElementScope")}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedElementLayoutProvider(
    content: @Composable () -> Unit
) {
    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this, content)
    }
}

val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope> { error("No AnimatedVisibilityScope") }

@Composable
fun AnimatedVisibilityScope.Provider(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAnimatedVisibilityScope provides this, content)
}