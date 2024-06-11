package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/6/1 10:52
 */


internal fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
    val copy = data.copyOf()
    for (index in copy.indices) {
        copy[index] = (data[index] + key[index % key.size]).toByte()
    }
    return copy
}

internal fun decrypt(data: ByteArray, key: ByteArray): ByteArray {
    val copy = data.copyOf()
    for (index in copy.indices) {
        copy[index] = (data[index] - key[index % key.size]).toByte()
    }
    return copy
}