package com.chat.compose.app.di

import com.chat.compose.app.usecase.*
import com.chat.compose.app.usecase.network.LoginUserUseCase
import com.chat.compose.app.usecase.network.RegistrationUseCase
import com.chat.compose.app.usecase.network.UserAccountCheckForRegistrationUseCase
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/19 00:17
 */
val useCaseModule = module {
    factory { OpenChatSessionUseCase(get()) }
    factory { CloseSessionUseCase(get()) }
    factory { FetchSessionContactUseCase(get(), get(), get()) }
    factory { FetchSessionListUseCase() }
    factory { FetchChatDetailListUseCase(get()) }
    factory { UpdateSessionContactUserBasicInfoUseCase(get()) }
    factory { SearchHistoryUseCase() }
}