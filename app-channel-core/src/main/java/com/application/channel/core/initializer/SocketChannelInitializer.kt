package com.application.channel.core.initializer

import com.application.channel.core.InitAdapter
import com.application.channel.core.handler.codc.ChannelCollectorCodec
import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.SocketChannelInitConfig
import io.netty.bootstrap.AbstractBootstrap
import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.group.ChannelGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel

/**
 * @author liuzhongao
 * @since 2024/5/26 20:57
 */
class SocketChannelInitializer(
    private val channelGroup: ChannelGroup,
    private val initConfig: SocketChannelInitConfig
) : ChannelInitializer {

    override fun <T : AbstractBootstrap<*, *>> initialize(
        bootstrap: T,
        init: (T, InitConfig) -> Unit
    ) {
        val channelInitializer = NettyChannelInitializer(
            channelGroup = this.channelGroup,
            initAdapter = this.initConfig.initAdapter
        )
        if (bootstrap is Bootstrap) {
            bootstrap.channel(NioSocketChannel::class.java)
                .handler(channelInitializer)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .option(ChannelOption.SO_KEEPALIVE, true)
            init(bootstrap, this.initConfig)
        } else if (bootstrap is ServerBootstrap) {
            bootstrap.childHandler(channelInitializer)
            init(bootstrap, this.initConfig)
        }
    }

    private class NettyChannelInitializer(
        private val channelGroup: ChannelGroup,
        private val initAdapter: InitAdapter?
    ) : io.netty.channel.ChannelInitializer<SocketChannel>() {
        override fun initChannel(ch: SocketChannel?) {
            val pipeline = ch?.pipeline() ?: return
            pipeline.addLast("channelCollector", ChannelCollectorCodec(this.channelGroup))

            // encoder only

            // decoder only

            // custom
            this.initAdapter?.dataTransformDecoderFactory
                ?.instantiate()
                ?.also { decoders -> pipeline.addLast(*decoders) }

            this.initAdapter?.dataTransformEncoderFactory
                ?.instantiate()
                ?.also { encoders -> pipeline.addLast(*encoders) }

            this.initAdapter?.dataHandler
                ?.instantiate()
                ?.also { handlers -> pipeline.addLast(*handlers) }
        }
    }

}