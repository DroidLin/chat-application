package com.chat.compose.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author liuzhongao
 * @since 2024/6/30 13:00
 */
val LocalSafeAreaConfig = staticCompositionLocalOf { SafeAreaConfig() }

class SafeAreaConfig {

    var navigationRailWidth: Dp by mutableStateOf(0.dp)
}

fun Modifier.applyNavigationRailSize(): Modifier = composed {
    val safeAreaConfig = LocalSafeAreaConfig.current
    val density = LocalDensity.current
    onSizeChanged { size ->
        safeAreaConfig.navigationRailWidth = with(density) { size.width.toDp() }
    }
}

fun Modifier.navigationRailPadding(): Modifier = composed {
    val safeAreaConfig = LocalSafeAreaConfig.current
    padding(start = safeAreaConfig.navigationRailWidth)
}