package com.chat.compose.app.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    navigationBarContent: @Composable RowScope.() -> Unit,
    navigationRailContent: @Composable ColumnScope.() -> Unit,
    navigationDrawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val navigationBarContentUpdated by rememberUpdatedState(navigationBarContent)
    val navigationRailContentUpdated by rememberUpdatedState(navigationRailContent)
    val navigationDrawerContentUpdated by rememberUpdatedState(navigationDrawerContent)
    val contentUpdated by rememberUpdatedState(content)
    val showNavigationUpdated by rememberUpdatedState(showNavigation)
    Box(
        modifier = modifier
    ) {
        val windowAdaptiveInfo = LocalWindowAdaptiveInfo.current
        when (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.EXPANDED -> {
                PermanentNavigationDrawer(
                    drawerContent = navigationDrawerContentUpdated,
                    content = contentUpdated
                )
            }

            WindowWidthSizeClass.MEDIUM -> {
                contentUpdated()
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .applyAppSafeArea(),
                    visible = showNavigationUpdated,
                    enter = slideInHorizontally { -it },
                    exit = slideOutHorizontally { -it }
                ) {
                    NavigationRail(
                        modifier = Modifier.fillMaxHeight(),
                        content = navigationRailContentUpdated
                    )
                }
            }

            WindowWidthSizeClass.COMPACT -> {
                contentUpdated()
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .applyAppSafeArea(),
                    visible = showNavigationUpdated,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    NavigationBar(
                        modifier = Modifier.fillMaxWidth(),
                        content = navigationBarContentUpdated
                    )
                }
            }

            else -> {}
        }
    }
}