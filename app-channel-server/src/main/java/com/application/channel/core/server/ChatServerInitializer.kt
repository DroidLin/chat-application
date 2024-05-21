package com.application.channel.core.server

import com.application.channel.core.InitAdapter
import com.application.channel.core.handler.*
import com.application.channel.core.handler.codc.ChannelCollectorCodec
import com.application.channel.core.rawDecoderList
import io.netty.channel.ChannelInitializer
import io.netty.channel.group.ChannelGroup
import io.netty.channel.socket.SocketChannel

/**
 * @author liuzhongao
 * @since 2024/5/12 19:43
 */
internal class ChatServerInitializer(
    private val channelGroup: ChannelGroup,
    private val initAdapter: InitAdapter?
) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel?) {
        val channelPipeline = ch?.pipeline() ?: return
        // encoder and decoder
        channelPipeline.addLast("channelCollector", ChannelCollectorCodec(this.channelGroup))
//        channelPipeline.addLast("byteArrayTransformer", ByteArrayCodec())
//        channelPipeline.addLast("byteArrayHandler", ByteArrayHandler(this.channelEventListener))
//        channelPipeline.addLast("stringTransformer", StringCodec())

        // encoder only

        // decoder only

        // custom
        val decoderList = this.initAdapter
            ?.dataTransformDecoderFactory
            ?.instantiate()
        decoderList?.forEach(channelPipeline::addLast)

        val encoderList = this.initAdapter
            ?.dataTransformEncoderFactory
            ?.instantiate()
        encoderList?.forEach(channelPipeline::addLast)

        val dataHandlerList = this.initAdapter
            ?.dataHandler
            ?.instantiate()
        dataHandlerList?.forEach(channelPipeline::addLast)
    }
}