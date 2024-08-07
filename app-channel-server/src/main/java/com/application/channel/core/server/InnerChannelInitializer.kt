package com.application.channel.core.server

import com.application.channel.core.InitAdapter
import com.application.channel.core.handler.codc.ChannelCollectorCodec
import io.netty.channel.ChannelInitializer
import io.netty.channel.group.ChannelGroup
import io.netty.channel.socket.SocketChannel

internal class InnerChannelInitializer(
    private val channelGroup: ChannelGroup,
    private val initAdapter: InitAdapter?
) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline() ?: return
        pipeline.addLast("channelCollector", ChannelCollectorCodec(this.channelGroup))

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
