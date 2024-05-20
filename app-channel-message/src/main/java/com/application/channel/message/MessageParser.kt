package com.application.channel.message

import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/5/7 00:02
 */
interface MessageParser {

    fun parse(jsonObject: JSONObject): Message
}