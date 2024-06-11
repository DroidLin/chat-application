package com.application.channel.message.transformer

import com.application.channel.core.DataTransformDecoder
import com.application.channel.core.DataTransformEncoder
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.MessageParser
import com.application.channel.message.meta.Messages.encodeToJSONObject
import io.netty.channel.ChannelHandler.Sharable
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/6/1 18:14
 */
@Sharable
class JSONObjectToMessageDecoder(
    private val parser: MessageParser
) : DataTransformDecoder<JSONObject, Message>() {

    override fun decode(msg: JSONObject, out: MutableList<Message>) {
        val message = this.parser.parse(msg) ?: return
        out += message
    }
}

class MessageToJSONObjectEncoder : DataTransformEncoder<Message, JSONObject>() {
    override fun encode(msg: Message, out: MutableList<JSONObject>) {
        out += msg.encodeToJSONObject()
    }
}