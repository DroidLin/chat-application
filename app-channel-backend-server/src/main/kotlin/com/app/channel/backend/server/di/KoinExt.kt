package com.app.channel.backend.server.di

import org.koin.core.context.GlobalContext

/**
 * @author liuzhongao
 * @since 2024/6/26 23:58
 */
inline fun <reified T> koinInject(): T = GlobalContext.get().get()