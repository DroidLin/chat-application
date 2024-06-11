package com.application.channel.message.encryptor

import com.application.channel.core.DataTransformDecoder
import com.application.channel.message.decrypt
import io.netty.channel.ChannelHandler.Sharable

/**
 * @author liuzhongao
 * @since 2024/5/31 00:08
 */
@Sharable
class DecryptorDecoder(private val key: ByteArray) : DataTransformDecoder<ByteArray, ByteArray>() {
    override fun decode(msg: ByteArray, out: MutableList<ByteArray>) {
        out += decrypt(msg, this.key)
    }
}