package com.chat.compose.app.screen.login

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.dependencies.chat.android.R
import com.chat.compose.app.router.LocalRouterAction
import com.chat.compose.app.ui.NavRoute

@Composable
actual fun LoginBottomCustomArea(modifier: Modifier) {
    val routerAction = LocalRouterAction.current
    Row(modifier = modifier) {
        TextButton(
            onClick = {
                routerAction.navigateTo(NavRoute.LoginRoute.RegisterAccount.route)
            }
        ) {
            Text(text = stringResource(R.string.string_register))
        }
    }
}