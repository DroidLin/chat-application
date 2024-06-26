package com.app.channel.backend.server.di

import com.app.channel.backend.server.router.login.LoginService
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/26 23:59
 */
val LoginModule = module {
    single { LoginService(get(), get(), get()) }
}