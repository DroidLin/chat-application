package com.chat.compose.app.di

import com.chat.compose.app.usecase.*
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/19 00:17
 */
val useCaseModule = module {
    factory { OpenChatSessionUseCase(get()) }
    factory { CloseSessionUseCase(get()) }
    factory { FetchRecentContactUseCase(get(), get(), get()) }
    factory { FetchSessionContactUseCase(get(), get(), get()) }
    factory { FetchRecentContactListUseCase() }
    factory { FetchChatDetailListUseCase(get(), get()) }
    factory { UpdateSessionContactUserBasicInfoUseCase(get()) }
    factory { SearchHistoryUseCase() }
    factory { InsertSessionContactUseCase(get()) }
    factory { InsertRecentContactUseCase(get()) }
}