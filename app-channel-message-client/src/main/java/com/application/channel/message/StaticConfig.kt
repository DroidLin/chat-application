package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/6/1 16:21
 */
data class StaticConfig(val encryptKey: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StaticConfig

        return encryptKey.contentEquals(other.encryptKey)
    }

    override fun hashCode(): Int {
        return encryptKey.contentHashCode()
    }
}