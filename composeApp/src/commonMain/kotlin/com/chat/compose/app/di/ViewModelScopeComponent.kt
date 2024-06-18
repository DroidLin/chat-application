package com.chat.compose.app.di

import com.chat.compose.app.screen.message.vm.SessionDetailViewModel
import com.chat.compose.app.screen.message.vm.SessionListViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/17 23:49
 */
@Singleton
@Component(
    modules = [
        MessageModule::class
    ]
)
interface ViewModelScopeComponent {

    val sessionListViewModel: SessionListViewModel

    val sessionDetailViewModel: SessionDetailViewModel
}