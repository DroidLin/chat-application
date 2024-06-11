package com.application.channel.message.encryptor

import com.application.channel.core.DataTransformEncoder
import com.application.channel.message.encrypt
import io.netty.channel.ChannelHandler.Sharable

/**
 * @author liuzhongao
 * @since 2024/5/31 00:02
 */
@Sharable
class EncryptorEncoder(private val key: ByteArray) : DataTransformEncoder<ByteArray, ByteArray>() {

    override fun encode(msg: ByteArray, out: MutableList<ByteArray>) {
        out += encrypt(msg, this.key)
    }
}