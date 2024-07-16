package com.chat.compose.app.util

import com.application.channel.core.util.koinInject
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONArray
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/6/29 19:12
 */
inline fun <reified T> fromJson(jsonObject: JSONObject): T? {
    return fromJson(jsonObject.toString())
}

inline fun <reified T> fromJson(jsonString: String): T? {
    val moshi = koinInject<Moshi>()
    val adapter = moshi.adapter(T::class.java)
    return kotlin.runCatching { adapter.fromJson(jsonString) }.getOrNull()
}

inline fun <reified T> fromJsonArray(jsonArray: JSONArray): List<T>? {
    return fromJsonArray(jsonArray.toString())
}

inline fun <reified T> fromJsonArray(jsonString: String): List<T>? {
    val moshi = koinInject<Moshi>()
    val types = Types.newParameterizedType(List::class.java, T::class.java)
    val adapter = moshi.adapter<List<T>>(types)
    return kotlin.runCatching { adapter.fromJson(jsonString) }.getOrNull()
}

inline fun <reified T> T.toJson(): String {
    val moshi = koinInject<Moshi>()
    val adapter = moshi.adapter<T>(T::class.java)
    return adapter.toJson(this)
}

inline fun <reified T> List<T>.toJson(): String {
    val moshi = koinInject<Moshi>()
    val types = Types.newParameterizedType(List::class.java, T::class.java)
    val adapter = moshi.adapter<List<T>>(types)
    return adapter.toJson(this)
}