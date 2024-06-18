package com.chat.compose.app.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.application.channel.im.IMInitConfig
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModel

/**
 * @author liuzhongao
 * @since 2024/6/18 01:03
 */

val LocalDaggerViewModelScopeComponent = compositionLocalOf<ViewModelScopeComponent> { error("not provided.") }

@Composable
expect fun viewModelScopeComponent(): ViewModelScopeComponent

@Composable
fun DaggerViewModelProviderScope(content: @Composable () -> Unit) {
    val component = viewModelScopeComponent()
    CompositionLocalProvider(LocalDaggerViewModelScopeComponent provides component, content = content)
}

inline fun <reified T : ViewModel> daggerViewModel(crossinline creator: (component: ViewModelScopeComponent) -> T): T {
    val component = requireNotNull(LocalDaggerViewModelScopeComponent.current)
    return viewModel(keys = listOf(component)) { creator(component) }
}