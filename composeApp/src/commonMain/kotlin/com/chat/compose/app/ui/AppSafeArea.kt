package com.chat.compose.app.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * @author liuzhongao
 * @since 2024/7/16 23:46
 */
val LocalAppSafeArea = compositionLocalOf<AppSafeArea> { AppSafeArea() }

@Stable
class AppSafeArea {

    var appNavigationSize: DpSize by mutableStateOf(DpSize.Zero)

    var navigationWidth: Dp by mutableStateOf(0.dp)
    var navigationHeight: Dp by mutableStateOf(0.dp)
}

@Stable
expect fun Modifier.applyAppSafeArea(): Modifier

@Stable
expect fun Modifier.appSafeAreaPadding(): Modifier