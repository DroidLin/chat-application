package com.application.channel.core.handler.encoder

import com.application.channel.core.DataTransformDecoder
import com.application.channel.core.DataTransformEncoder

/**
 * @author liuzhongao
 * @since 2024/5/21 00:33
 */
class MessageStringToMessageByteArrayEncoder : DataTransformEncoder<String, ByteArray>() {
    override fun encode(msg: String, out: MutableList<ByteArray>) {
        out += msg.encodeToByteArray()
    }
}

class MessageByteArrayToMessageStringDecoder : DataTransformDecoder<ByteArray, String>() {
    override fun decode(msg: ByteArray, out: MutableList<String>) {
        out += msg.decodeToString()
    }
}