package com.chat.compose.app.di

import com.chat.compose.app.screen.message.vm.SessionDetailViewModel
import com.chat.compose.app.screen.message.vm.SessionListViewModel
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/18 23:53
 */
val viewModelModule = module {
    factory {
        SessionListViewModel(get())
    }
    factory {
        SessionDetailViewModel(get(), get())
    }
}