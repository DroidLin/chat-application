package com.application.channel.core.client

import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/23 02:32
 */
val TcpClientModule = module {
    single { TcpClient() }
}