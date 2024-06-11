package com.application.channel.message.meta

import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/5/7 00:02
 */
interface MessageParser {

    fun parse(jsonObject: JSONObject): Message?

    fun parse(message: Message): Message?
}

fun MessageParser(parsers: List<MessageParser>): MessageParser = MessageParserImpl(parsers)

internal class MessageParserImpl(private val parserList: List<MessageParser>) : MessageParser {
    override fun parse(jsonObject: JSONObject): Message? {
        return this.parserList.firstNotNullOfOrNull { parser ->
            parser.parse(jsonObject)
        }
    }

    override fun parse(message: Message): Message? {
        return this.parserList.firstNotNullOfOrNull { parser ->
            parser.parse(message)
        }
    }
}