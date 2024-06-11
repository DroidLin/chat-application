package com.application.channel.core.handler.encoder

import com.application.channel.core.DataTransformDecoder
import com.application.channel.core.DataTransformEncoder
import io.netty.channel.ChannelHandler.Sharable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInput
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/5/21 00:33
 */
private const val FLAG_STRING = 0x11

@Sharable
class StringToByteArrayEncoder @Inject constructor() : DataTransformEncoder<String, ByteArray>() {
    override fun encode(msg: String, out: MutableList<ByteArray>) {
        ByteArrayOutputStream().use { outputStream ->
            ObjectOutputStream(outputStream).use { objOutputStream ->
                objOutputStream.writeInt(FLAG_STRING)
                objOutputStream.writeUTF(msg)
            }
            out += outputStream.toByteArray()
        }
    }
}

@Sharable
class ByteArrayToStringDecoder @Inject constructor() : DataTransformDecoder<ByteArray, String>() {
    override fun decode(msg: ByteArray, out: MutableList<String>) {
        ObjectInputStream(ByteArrayInputStream(msg)).use { objectInputStream ->
            val flag = objectInputStream.readInt()
            if (flag != FLAG_STRING) {
                return
            }
            out += objectInputStream.readUTF()
        }
    }
}