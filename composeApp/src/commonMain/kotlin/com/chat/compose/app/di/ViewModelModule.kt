package com.chat.compose.app.di

import com.chat.compose.app.screen.login.LoginViewModel
import com.chat.compose.app.screen.login.RegisterAccountViewModel
import com.chat.compose.app.screen.message.vm.SessionDetailViewModel
import com.chat.compose.app.screen.message.vm.SessionListViewModel
import com.chat.compose.app.screen.search.SearchLauncherViewModel
import com.chat.compose.app.screen.search.SearchResultViewModel
import com.chat.compose.app.screen.user.PersonalInfoViewModel
import com.chat.compose.app.screen.user.UserBasicInfoViewModel
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/18 23:53
 */
val viewModelModule = module {
    factory { SessionListViewModel(get(), get(), get()) }
    factory { SessionDetailViewModel(get(), get(), get(), get()) }
    factory { LoginViewModel(get(), get()) }
    factory { RegisterAccountViewModel(get(), get(), get(), get()) }
    factory { SearchLauncherViewModel(get()) }
    factory { SearchResultViewModel(get()) }
    factory { UserBasicInfoViewModel(get(), get()) }
    factory { PersonalInfoViewModel(get()) }
}