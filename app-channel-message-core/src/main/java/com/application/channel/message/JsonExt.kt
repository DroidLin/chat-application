package com.application.channel.message

import org.json.JSONArray
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/5/30 21:58
 */

val JSONObject.map: Map<String, Any?>
    get() {
        val map = hashMapOf<String, Any?>()
        this.keys().forEach { key ->
            map[key] = when (val value = this.opt(key)) {
                is JSONObject -> value.map
                is JSONArray -> value.list
                else -> value
            }
        }
        return map
    }

val JSONArray.list: List<Any?>
    get() {
        val list = ArrayList<Any?>()
        for (index in 0 until this.length()) {
            list += when (val obj = this.opt(index)) {
                is JSONObject -> obj.map
                is JSONArray -> obj.list
                else -> obj
            }
        }
        return list
    }