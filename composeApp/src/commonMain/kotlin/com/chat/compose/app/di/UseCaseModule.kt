package com.chat.compose.app.di

import com.chat.compose.app.usecase.FakeFetchSessionListUseCase
import com.chat.compose.app.usecase.FetchSessionListUseCase
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/19 00:17
 */

val useCaseModule = module {
    factory<FetchSessionListUseCase> {
        FakeFetchSessionListUseCase()
    }
}