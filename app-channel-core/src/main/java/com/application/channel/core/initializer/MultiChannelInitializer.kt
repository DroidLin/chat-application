package com.application.channel.core.initializer

import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.MultiInitConfig
import io.netty.bootstrap.AbstractBootstrap
import io.netty.channel.group.ChannelGroup

/**
 * @author liuzhongao
 * @since 2024/5/26 23:23
 */
class MultiChannelInitializer(
    private val channelGroup: ChannelGroup,
    private val initConfig: MultiInitConfig,
) : ChannelInitializer {

    override fun <T : AbstractBootstrap<*, *>> initialize(
        bootstrap: T,
        init: (T, InitConfig) -> Unit
    ) {
        this.initConfig.initConfigList.forEach { initConfig ->
            ChannelInitializerFactory.create(this.channelGroup, initConfig)
                .initialize(bootstrap, init)
        }
    }

}