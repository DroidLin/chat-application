package com.application.channel.core.initializer

import com.application.channel.core.InitAdapter
import com.application.channel.core.handler.codc.ChannelCollectorCodec
import com.application.channel.core.model.DatagramChannelInitConfig
import com.application.channel.core.model.InitConfig
import io.netty.bootstrap.AbstractBootstrap
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.group.ChannelGroup
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.nio.NioDatagramChannel

/**
 * @author liuzhongao
 * @since 2024/5/27 23:41
 */
class DatagramChannelInitializer(
    private val channelGroup: ChannelGroup,
    private val initConfig: DatagramChannelInitConfig
) : ChannelInitializer {
    override fun <T : AbstractBootstrap<*, *>> initialize(bootstrap: T, init: (T, InitConfig) -> Unit) {
        if (bootstrap is Bootstrap) {
            val channelInitializer = NettyDatagramChannelInitializer(
                channelGroup = this.channelGroup,
                initAdapter = this.initConfig.initAdapter
            )
            bootstrap.channel(NioDatagramChannel::class.java)
                .handler(channelInitializer)
                .option(ChannelOption.SO_BROADCAST, this.initConfig.broadcast)
                .localAddress(this.initConfig.localSocketAddress)
                .remoteAddress(this.initConfig.remoteSocketAddress)
            init(bootstrap, initConfig)
        }
    }

    private class NettyDatagramChannelInitializer(
        private val channelGroup: ChannelGroup,
        private val initAdapter: InitAdapter?
    ) : io.netty.channel.ChannelInitializer<DatagramChannel>() {
        override fun initChannel(ch: DatagramChannel?) {
            val pipeline = ch?.pipeline() ?: return
            pipeline.addLast(ChannelCollectorCodec(this.channelGroup))
            // encoder only

            // decoder only

            // custom
            this.initAdapter?.dataTransformDecoderFactory
                ?.instantiate()
                ?.also { decoders -> pipeline.addLast(*decoders.toTypedArray()) }

            this.initAdapter?.dataTransformEncoderFactory
                ?.instantiate()
                ?.also { encoders -> pipeline.addLast(*encoders.toTypedArray()) }

            this.initAdapter?.dataHandler
                ?.instantiate()
                ?.also { handlers -> pipeline.addLast(*handlers.toTypedArray()) }
        }
    }
}