package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/3/28 00:19
 */
interface MutableMapStorage : MapStorage {

    fun putInt(key: String, value: Int)

    fun putLong(key: String, value: Long)

    fun putFloat(key: String, value: Float)

    fun putDouble(key: String, value: Double)

    fun putBoolean(key: String, value: Boolean)

    fun putByte(key: String, value: Byte)

    fun putChar(key: String, value: Char)

    fun putShort(key: String, value: Short)

    fun putString(key: String, value: String?)

    fun flush()
}