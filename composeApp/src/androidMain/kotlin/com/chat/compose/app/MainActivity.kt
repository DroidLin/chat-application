package com.chat.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.chat.compose.app.platform.ui.FrameworkScreen
import com.chat.compose.app.platform.ui.WindowAdaptiveInfoProvider
import com.chat.compose.app.ui.AndroidMaterialTheme
import com.chat.compose.app.ui.AppSafeArea
import com.chat.compose.app.ui.LocalAppSafeArea

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { error("No activity in LocalActivity") }

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(this.window, false)
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalActivity provides this,
                LocalAppSafeArea provides remember { AppSafeArea() }
            ) {
                WindowAdaptiveInfoProvider {
                    AndroidMaterialTheme(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        FrameworkScreen()
                    }
                }
            }
        }
    }
}
