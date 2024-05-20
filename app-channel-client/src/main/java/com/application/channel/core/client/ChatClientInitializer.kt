package com.application.channel.core.client

import com.application.channel.core.InitAdapter
import com.application.channel.core.handler.codc.ChannelCollectorCodec
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.group.ChannelGroup
import io.netty.channel.socket.SocketChannel

/**
 * @author liuzhongao
 * @since 2024/5/13 22:47
 */
internal class ChatClientInitializer(
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
            ?.invoke()
        decoderList?.forEach(channelPipeline::addLast)

        val encoderList = this.initAdapter
            ?.dataTransformEncoderFactory
            ?.invoke()
        encoderList?.forEach(channelPipeline::addLast)

        val dataHandlerList = this.initAdapter
            ?.dataHandler
            ?.invoke()
        dataHandlerList?.forEach(channelPipeline::addLast)
    }

    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        cause?.printStackTrace()
        super.exceptionCaught(ctx, cause)
    }
}