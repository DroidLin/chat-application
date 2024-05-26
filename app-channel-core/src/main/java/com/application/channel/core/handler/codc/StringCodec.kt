package com.application.channel.core.handler.codc

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec

/**
 * @author liuzhongao
 * @since 2024/5/15 01:40
 */
class StringCodec : MessageToMessageCodec<ByteArray, String>() {
    override fun encode(ctx: ChannelHandlerContext?, msg: String?, out: MutableList<Any>?) {
        if (ctx == null || msg == null || out == null) return
        out += msg.encodeToByteArray()
    }

    override fun decode(ctx: ChannelHandlerContext?, msg: ByteArray?, out: MutableList<Any>?) {
        if (ctx == null || msg == null || out == null) return
        out += msg.decodeToString()
    }
}