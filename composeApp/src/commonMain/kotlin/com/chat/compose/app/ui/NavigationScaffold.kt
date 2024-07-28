package com.chat.compose.app.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowWidthSizeClass
import com.chat.compose.app.platform.ui.LocalWindowAdaptiveInfo
import com.chat.compose.app.ui.framework.Box

/**
 * @author liuzhongao
 * @since 2024/7/26 00:57
 */
@Composable
fun NavigationScaffold(
    showNavigation: Boolean,
    modifier: Modifier = Modifier,
    navigationContent: @Composable Any.() -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        content()
        val windowAdaptiveInfo = LocalWindowAdaptiveInfo.current
        when (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.EXPANDED -> {
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .applyAppSafeArea(),
                    visible = showNavigation,
                    enter = slideInHorizontally { -it },
                    exit = slideOutHorizontally { -it }
                ) {
                    NavigationRail(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        navigationContent()
                    }
                }
            }

            else -> {
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .applyAppSafeArea(),
                    visible = showNavigation,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    NavigationBar(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        navigationContent()
                    }
                }
            }
        }
    }
}