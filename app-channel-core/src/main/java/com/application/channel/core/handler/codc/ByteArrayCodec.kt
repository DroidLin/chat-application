package com.application.channel.core.handler.codc

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import io.netty.util.ReferenceCountUtil
import java.io.ByteArrayOutputStream

/**
 * @author liuzhongao
 * @since 2024/5/11 00:57
 */
class ByteArrayCodec : MessageToMessageCodec<ByteBuf, ByteArray>() {
    override fun encode(ctx: ChannelHandlerContext?, msg: ByteArray?, out: MutableList<Any>?) {
        if (ctx == null || out == null || msg == null) {
            return
        }
        val byteBuf = ctx.alloc().buffer().also { byteBuf ->
            byteBuf.writeInt(msg.size)
            byteBuf.writeBytes(msg)
        }
        out += ReferenceCountUtil.retain(byteBuf)
    }

    override fun decode(ctx: ChannelHandlerContext?, msg: ByteBuf?, out: MutableList<Any>?) {
        if (ctx == null || out == null || msg == null || !msg.isReadable) {
            return
        }
        while (msg.isReadable) {
            var shouldResetReaderIndex = false
            val readerIndex = msg.readerIndex()
            val dataCount = msg.withAvailableCount(4) { this.readInt() }
            if (dataCount != null) {
                val byteArray = msg.withAvailableCount(dataCount) {
                    ByteArrayOutputStream().use { outputStream ->
                        for (index in 0 until dataCount) {
                            outputStream.write(this.readByte().toInt())
                        }
                        outputStream.toByteArray()
                    }
                }
                if (byteArray != null) {
                    out += ReferenceCountUtil.retain(byteArray)
                } else shouldResetReaderIndex = true
            } else shouldResetReaderIndex = true

            if (shouldResetReaderIndex) {
                msg.readerIndex(readerIndex)
            }
        }
    }

    private inline fun <T : Any> ByteBuf.withAvailableCount(count: Int, function: ByteBuf.() -> T): T? {
        this.markReaderIndex()
        val readableCount = this.readableBytes()
        if (readableCount >= count) {
            return function()
        }
        this.resetReaderIndex()
        return null
    }

}