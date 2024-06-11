package com.application.channel.message.transformer

import com.application.channel.core.DataTransformDecoder
import com.application.channel.core.DataTransformEncoder
import com.application.channel.core.model.StringWritable
import com.application.channel.core.model.Writable
import com.application.channel.message.meta.Message
import com.application.channel.message.MessageWritable
import com.application.channel.message.meta.Messages.encodeToJSONObject
import io.netty.channel.ChannelHandler.Sharable
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * @author liuzhongao
 * @since 2024/6/1 10:45
 */

private const val FLAG_STRING = 1 shl 0
private const val FLAG_JSON_STRING = 1 shl 1

@Sharable
class ByteArrayToObjectDecoder : DataTransformDecoder<ByteArray, Any>() {

    override fun decode(msg: ByteArray, out: MutableList<Any>) {
        ObjectInputStream(ByteArrayInputStream(msg)).use { objectInputStream ->
            val flag = objectInputStream.readInt()
            when (flag) {
                FLAG_STRING -> this.decodeString(objectInputStream, out)
                FLAG_JSON_STRING -> this.decodeJSONString(objectInputStream, out)
            }
        }
    }

    private fun decodeString(inputStream: ObjectInputStream, out: MutableList<Any>) {
        out += inputStream.readUTF()
    }

    private fun decodeJSONString(inputStream: ObjectInputStream, out: MutableList<Any>) {
        val jsonString = inputStream.readUTF()
        if (jsonString.isEmpty() || jsonString.isBlank()) {
            return
        }

        val jsonObject = kotlin.runCatching { JSONObject(jsonString) }
            .onFailure { it.printStackTrace() }.getOrNull() ?: return
        out += jsonObject
    }
}

@Sharable
class ObjectToByteArrayEncoder : DataTransformEncoder<Writable, ByteArray>() {

    override fun encode(msg: Writable, out: MutableList<ByteArray>) {
        when (msg) {
            is StringWritable -> this.encodeString(msg.value, out)
            is MessageWritable -> this.encodeMessage(msg.message, out)
        }
    }

    private fun encodeString(msg: String, out: MutableList<ByteArray>) {
        ByteArrayOutputStream().use { outputStream ->
            ObjectOutputStream(outputStream).use { objOutputStream ->
                objOutputStream.writeInt(FLAG_STRING)
                objOutputStream.writeUTF(msg)
            }
            out += outputStream.toByteArray()
        }
    }

    private fun encodeMessage(msg: Message, out: MutableList<ByteArray>) {
        ByteArrayOutputStream().use { outputStream ->
            ObjectOutputStream(outputStream).use { objOutputStream ->
                objOutputStream.writeInt(FLAG_JSON_STRING)
                objOutputStream.writeUTF(msg.encodeToJSONObject().toString())
            }
            out += outputStream.toByteArray()
        }
    }
}