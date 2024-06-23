package com.application.channel.message

import org.koin.dsl.module
import java.util.logging.Logger

/**
 * @author liuzhongao
 * @since 2024/6/23 11:35
 */

val ChatServiceModule = module {
    single { ChatService(get(), get(), get()) }
    single { Authorization() }
    single { Logger.getLogger("CommonClient") }
}