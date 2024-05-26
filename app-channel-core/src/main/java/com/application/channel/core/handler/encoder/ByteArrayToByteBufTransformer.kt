package com.application.channel.core.handler.encoder

import com.application.channel.core.DataTransformDecoder
import com.application.channel.core.DataTransformEncoder
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.io.ByteArrayOutputStream

/**
 * @author liuzhongao
 * @since 2024/5/21 00:45
 */

private const val FLAG_MESSAGE_BYTE_ARRAY = 0x10101

class ByteArrayToByteBufEncoder : DataTransformEncoder<ByteArray, ByteBuf>() {
    override fun encode(msg: ByteArray, out: MutableList<ByteBuf>) {
        out += Unpooled.buffer().also { byteBuf ->
            byteBuf.writeInt(FLAG_MESSAGE_BYTE_ARRAY)
            byteBuf.writeInt(msg.size)
            byteBuf.writeBytes(msg)
        }
    }
}

class ByteBufToByteArrayDecoder : DataTransformDecoder<ByteBuf, ByteArray>() {
    private val _byteArrayOutputStream = ByteArrayOutputStream()

    override fun decode(msg: ByteBuf, out: MutableList<ByteArray>) {
        while (msg.isReadable) {
            var shouldResetReaderIndex = false
            val readerIndex = msg.readerIndex()
            val dataType = msg.withAvailableCount(Int.SIZE_BYTES) { this.readInt() }
            if (dataType != FLAG_MESSAGE_BYTE_ARRAY) {
                msg.readerIndex(readerIndex)
                break
            }
            val dataCount = msg.withAvailableCount(Int.SIZE_BYTES) { this.readInt() }
            if (dataCount != null) {
                val byteArray = msg.withAvailableCount(dataCount) {
                    this@ByteBufToByteArrayDecoder._byteArrayOutputStream.reset()
                    this@ByteBufToByteArrayDecoder._byteArrayOutputStream.use { outputStream ->
                        for (index in 0 until dataCount) {
                            outputStream.write(this.readByte().toInt())
                        }
                        outputStream.toByteArray()
                    }
                }
                if (byteArray != null) {
                    out += byteArray
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