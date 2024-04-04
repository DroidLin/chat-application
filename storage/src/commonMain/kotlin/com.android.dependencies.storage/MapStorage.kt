package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/3/27 23:21
 */
interface MapStorage : FileStorage {

    /**
     * copies of inner map
     */
    val all: Map<String, Any?>

    fun contains(key: String): Boolean

    fun getInt(key: String, default: Int): Int

    fun getLong(key: String, default: Long): Long

    fun getFloat(key: String, default: Float): Float

    fun getDouble(key: String, default: Double): Double

    fun getBoolean(key: String, default: Boolean): Boolean

    fun getByte(key: String, default: Byte): Byte

    fun getChar(key: String, default: Char): Char

    fun getShort(key: String, default: Short): Short

    fun getString(key: String, default: String?): String?

}