package com.chat.compose.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowCompat
import com.chat.compose.app.ui.AndroidMaterialTheme
import com.chat.compose.app.ui.FrameworkScreen

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { error("No activity in LocalActivity") }

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(this.window, false)
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(LocalActivity provides this) {
                AndroidMaterialTheme {
                    FrameworkScreen()
                }
            }
        }
    }
}
