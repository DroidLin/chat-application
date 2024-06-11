package com.application.channel.core.model

/**
 * @author liuzhongao
 * @since 2024/5/28 21:31
 */
interface Writable

data class DatagramWritable(
    val value: ByteArray,
    val host: String,
    val port: Int
) : Writable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DatagramWritable

        return value.contentEquals(other.value)
    }

    override fun hashCode(): Int {
        return value.contentHashCode()
    }
}

data class ByteArrayWritable(val value: ByteArray) : Writable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ByteArrayWritable

        return value.contentEquals(other.value)
    }

    override fun hashCode(): Int {
        return value.contentHashCode()
    }
}

data class StringWritable(val value: String) : Writable