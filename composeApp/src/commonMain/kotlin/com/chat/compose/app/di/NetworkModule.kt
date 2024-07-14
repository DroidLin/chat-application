package com.chat.compose.app.di

import com.chat.compose.app.network.NetworkCookieStorage
import com.chat.compose.app.usecase.network.*
import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/29 14:06
 */
val NetworkModule = module {
    single {
        HttpClient {
            install(HttpCookies) {
                storage = NetworkCookieStorage()
            }
            engine {
            }
        }
    }
    factory { FetchUserInfoUseCase(get()) }
    factory { LoginUserUseCase(get()) }
    factory { UserAccountCheckForRegistrationUseCase(get()) }
    factory { RegistrationUseCase(get()) }
    factory { SearchComplexUseCase(get()) }
}