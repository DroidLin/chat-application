package com.chat.compose.app.di

import com.chat.compose.app.services.ProfileService
import com.squareup.moshi.Moshi
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/29 19:01
 */
val SerializationModule = module {
    single {
        Moshi.Builder().build()
    }
    single { ProfileService(get()) }
}