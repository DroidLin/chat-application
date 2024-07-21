package com.chat.compose.app.screen.placeholder

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.homeNavigationComposable

/**
 * @author liuzhongao
 * @since 2024/7/21 17:50
 */
fun NavGraphBuilder.emptyPlaceholder() {
    homeNavigationComposable(
        route = NavRoute.EmptyPlaceHolder.route
    ) {
        EmptyPlaceholder()
    }
}

@Composable
private fun EmptyPlaceholder() {

}