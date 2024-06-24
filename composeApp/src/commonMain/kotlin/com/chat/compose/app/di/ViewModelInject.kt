package com.chat.compose.app.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.channel.core.util.koinInject

/**
 * @author liuzhongao
 * @since 2024/6/18 23:50
 */

@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    return viewModel { koinInject() }
}