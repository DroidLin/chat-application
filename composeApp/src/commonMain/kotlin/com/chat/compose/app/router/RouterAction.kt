package com.chat.compose.app.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator

/**
 * @author liuzhongao
 * @since 2024/6/16 21:56
 */
@Stable
interface RouterAction {

    val navigator: Navigator

    val canGoBackState: StateFlow<Boolean>

    val canGoBack: Boolean

    fun navigateTo(route: String)

    fun navigateTo(route: String, navOptions: NavOptions?)

    fun backPress()
}

@Composable
fun rememberRouterAction(): RouterAction {
    val navigator = rememberNavigator()
    val coroutineScope = rememberCoroutineScope()
    return remember(navigator, coroutineScope) { RouterActionImpl(navigator, coroutineScope) }
}

@Stable
private class RouterActionImpl(
    override val navigator: Navigator,
    private val coroutineScope: CoroutineScope
) : RouterAction {

    override val canGoBackState: StateFlow<Boolean> = this.navigator.canGoBack
        .stateIn(this.coroutineScope, SharingStarted.Eagerly, false)

    override val canGoBack: Boolean
        get() = this.canGoBackState.value

    override fun navigateTo(route: String) {
        this.navigator.navigate(route)
    }

    override fun navigateTo(route: String, navOptions: NavOptions?) {
        this.navigator.navigate(route, navOptions)
    }

    override fun backPress() {
        if (this.canGoBackState.value) {
            this.navigator.popBackStack()
        }
    }

}