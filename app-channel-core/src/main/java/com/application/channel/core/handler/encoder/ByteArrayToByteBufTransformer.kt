package com.application.channel.core.handler.encoder

import com.application.channel.core.DataTransformDecoder
import com.application.channel.core.DataTransformEncoder
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandler.Sharable
import java.io.ByteArrayOutputStream
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

val ByteBuf.byteArray: ByteArray?
    get() {
        var byteArray: ByteArray? = null
        val outputStream: ByteArrayOutputStream = threadLocalOutputStream.get()
        while (this.isReadable) {
            var shouldResetReaderIndex = false
            val readerIndex = this.readerIndex()
            val dataType = this.withAvailableCount(Int.SIZE_BYTES) { this.readInt() }
            if (dataType != FLAG_MESSAGE_BYTE_ARRAY) {
                this.readerIndex(readerIndex)
                break
            }
            val dataCount = this.withAvailableCount(Int.SIZE_BYTES) { this.readInt() }
            if (dataCount != null) {
                val _byteArray = this.withAvailableCount(dataCount) {
                    outputStream.reset()
                    outputStream.use { outputStream ->
                        for (index in 0 until dataCount) {
                            outputStream.write(this.readByte().toInt())
                        }
                        outputStream.toByteArray()
                    }
                }
                if (_byteArray != null) {
                    byteArray = _byteArray
                } else shouldResetReaderIndex = true
            } else shouldResetReaderIndex = true

            if (shouldResetReaderIndex) {
                this.readerIndex(readerIndex)
            }
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
