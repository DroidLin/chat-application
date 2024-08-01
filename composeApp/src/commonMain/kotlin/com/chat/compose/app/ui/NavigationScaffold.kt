package com.chat.compose.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
        val windowSizeClass = windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass
        if (windowSizeClass == WindowWidthSizeClass.MEDIUM) {
            if (showNavigationUpdated) {
                NavigationRail(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .applyAppSafeArea(),
                    content = navigationRailContentUpdated
                )
            }
        }
        Row {
            if (windowSizeClass == WindowWidthSizeClass.EXPANDED) {
                navigationDrawerContentUpdated()
            }
            contentUpdated()
        }
        if (windowSizeClass == WindowWidthSizeClass.COMPACT) {
            if (showNavigationUpdated) {
                NavigationBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .applyAppSafeArea()
                        .fillMaxWidth(),
                    content = navigationBarContentUpdated
                )
            }
        }
    }
}