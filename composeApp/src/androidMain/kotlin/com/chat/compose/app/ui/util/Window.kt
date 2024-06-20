package com.chat.compose.app.ui.util

import android.os.Build
import android.view.View
import android.view.Window

fun Window.setLightStatusBar(light: Boolean) {
    if (light) {
        decorView.systemUiVisibility =
            decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        decorView.systemUiVisibility =
            decorView.systemUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
    }
}

fun Window.setLightNavigation(light: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (light) {
            decorView.systemUiVisibility =
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            decorView.systemUiVisibility =
                decorView.systemUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR).inv()
        }
    }
}