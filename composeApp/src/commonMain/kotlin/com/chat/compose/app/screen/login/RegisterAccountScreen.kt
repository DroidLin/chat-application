package com.chat.compose.app.screen.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.framework.Box
import com.chat.compose.app.ui.homeNavigationComposable

/**
 * @author liuzhongao
 * @since 2024/6/25 22:07
 */
fun NavGraphBuilder.registerAccountScreen() {
    homeNavigationComposable(
        route = NavRoute.LoginRoute.RegisterAccount.route
    ) {
        RegisterAccountScreen()
    }
}


@Composable
fun RegisterAccountScreen() {
    Box(modifier = Modifier.fillMaxSize()) {

    }
}