package com.application.channel.core.handler.encoder

import com.application.channel.core.DataTransformDecoder
import com.application.channel.core.DataTransformEncoder
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandler.Sharable
import java.io.ByteArrayOutputStream
import java.util.LinkedList
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/5/21 00:45
 */

private const val FLAG_MESSAGE_BYTE_ARRAY = 0x10101

val ByteArray.byteBuf: ByteBuf
    get() = Unpooled.buffer().also { byteBuf ->
        byteBuf.writeInt(FLAG_MESSAGE_BYTE_ARRAY)
        byteBuf.writeInt(this.size)
        byteBuf.writeBytes(this)
    }

@Sharable
class ByteArrayToByteBufEncoder @Inject constructor() : DataTransformEncoder<ByteArray, ByteBuf>() {
    override fun encode(msg: ByteArray, out: MutableList<ByteBuf>) {
        out += msg.byteBuf
    }
}

private val threadLocalOutputStream = object : ThreadLocal<ByteArrayOutputStream>() {
    override fun initialValue(): ByteArrayOutputStream = ByteArrayOutputStream()
}

val ByteBuf.byteArray: List<ByteArray>?
    get() {
        var byteArray: MutableList<ByteArray>? = null
        val outputStream: ByteArrayOutputStream = threadLocalOutputStream.get()
        var deadLoopMonitorCount = 0
        while (this.isReadable || deadLoopMonitorCount >= 5) {
            var shouldResetReaderIndex = false
            val readerIndex = this.readerIndex()
            val dataType = this.withAvailableCount(Int.SIZE_BYTES) { this.readInt() }
            if (dataType != FLAG_MESSAGE_BYTE_ARRAY) {
                this.readerIndex(readerIndex)
                break
            }
            val dataCount = this.withAvailableCount(Int.SIZE_BYTES) { this.readInt() }
            if (dataCount != null) {
                val data = this.withAvailableCount(dataCount) {
                    outputStream.reset()
                    outputStream.use { outputStream ->
                        for (index in 0 until dataCount) {
                            outputStream.write(this.readByte().toInt())
                        }
                        outputStream.toByteArray()
                    }
                }
                if (data != null) {
                    if (byteArray == null) {
                        byteArray = LinkedList<ByteArray>()
                    }
                    byteArray += data
                } else shouldResetReaderIndex = true
            } else shouldResetReaderIndex = true

            // we consider drop some unsupported bytes, if data breaks up with max dead loop monitor count reached.
            if (shouldResetReaderIndex) {
                this.readerIndex(readerIndex)
                deadLoopMonitorCount++
            } else break
        }
        return byteArray
    }

@Sharable
class ByteBufToByteArrayDecoder @Inject constructor() : DataTransformDecoder<ByteBuf, ByteArray>() {

    override fun decode(msg: ByteBuf, out: MutableList<ByteArray>) {
        val byteArray = msg.byteArray ?: return
        out += byteArray
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
