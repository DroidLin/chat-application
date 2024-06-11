package com.application.channel.core.handler.encoder

import com.application.channel.core.model.DatagramWritable
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.DefaultAddressedEnvelope
import io.netty.handler.codec.MessageToMessageEncoder
import java.net.InetSocketAddress
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/5/28 21:45
 */
@Sharable
class DatagramWritableToByteArrayEncoder @Inject constructor() : MessageToMessageEncoder<DatagramWritable>() {
    override fun encode(ctx: ChannelHandlerContext?, msg: DatagramWritable?, out: MutableList<Any>?) {
        if (ctx == null || msg == null || out == null) return
        out += DefaultAddressedEnvelope(msg, InetSocketAddress(msg.host, msg.port))
    }
}