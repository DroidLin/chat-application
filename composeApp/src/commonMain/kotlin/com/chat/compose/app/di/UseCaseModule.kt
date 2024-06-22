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
    factory { FetchSessionContactUseCase(get()) }
    factory { FetchSessionListUseCase(get()) }
    factory { FetchChatDetailListUseCase(get()) }
}