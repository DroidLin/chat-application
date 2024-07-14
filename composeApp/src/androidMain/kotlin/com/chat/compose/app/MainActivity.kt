package com.chat.compose.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.chat.compose.app.router.RouteAction
import com.chat.compose.app.router.rememberRouterAction
import com.chat.compose.app.ui.AndroidMaterialTheme
import com.chat.compose.app.ui.FrameworkScreen

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { error("No activity in LocalActivity") }

class MainActivity : ComponentActivity() {

    private var routeAction: RouteAction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(this.window, false)
        super.onCreate(savedInstanceState)

        setContent {
            val routeAction = rememberRouterAction()
            LaunchedEffect(routeAction) {
                this@MainActivity.routeAction = routeAction
            }
            CompositionLocalProvider(LocalActivity provides this) {
                AndroidMaterialTheme {
                    FrameworkScreen(routeAction)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        (this.routeAction?.navController as? NavHostController)?.handleDeepLink(intent)
    }
}
