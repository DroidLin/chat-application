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

@JvmOverloads
fun MessageParser(parsers: List<MessageParser> = emptyList()): MessageParser = MessageParser { this += parsers }

fun MessageParser(function: MutableList<MessageParser>.() -> Unit): MessageParser {
    val mutableList = mutableListOf<MessageParser>()
    mutableList.apply(function)
    mutableList += TextMessage.Parser()
    mutableList += ImageMessage.Parser()
    mutableList += LoginMessage.Parser()
    mutableList += LoginResultMessage.Parser()
    mutableList += LogoutMessage.Parser()
    mutableList += MessageImpl.Parser()
    return MessageParserImpl(mutableList)
}


private class MessageParserImpl(private val parserList: List<MessageParser>) : MessageParser {
    override fun parse(jsonObject: JSONObject): Message? {
        return this.parserList.firstNotNullOfOrNull { parser ->
            parser.parse(jsonObject)
        }
    }

    override fun parse(message: Message): Message {
        return this.parserList.firstNotNullOfOrNull { parser ->
            parser.parse(message)
        } ?: message
    }
}