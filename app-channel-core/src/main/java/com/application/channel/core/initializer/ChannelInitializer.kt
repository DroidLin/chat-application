package com.application.channel.core.initializer

import com.application.channel.core.model.InitConfig
import io.netty.bootstrap.AbstractBootstrap

/**
 * @author liuzhongao
 * @since 2024/5/26 20:55
 */
interface ChannelInitializer {

    fun <T : AbstractBootstrap<*, *>> initialize(
        bootstrap: T,
        init: (T, InitConfig) -> Unit
    )
}