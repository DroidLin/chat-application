package com.application.channel.message

import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/5/31 20:40
 */
interface ChainInterceptor {

    fun intercept(jsonObject: JSONObject)
}