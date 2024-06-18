package com.chat.compose.app.di

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.viewmodel.ViewModel
import org.koin.compose.koinInject

/**
 * @author liuzhongao
 * @since 2024/6/18 23:50
 */

@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    return koinInject<T>()
}