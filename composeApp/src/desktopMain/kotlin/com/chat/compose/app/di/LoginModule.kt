package com.chat.compose.app.di

import com.chat.compose.app.screen.login.LoginViewModel
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/25 00:25
 */
val LoginModule = module {
    factory { LoginViewModel() }
}