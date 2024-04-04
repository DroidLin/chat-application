package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/3/27 23:24
 */
interface FileStorage : Storage {

    /**
     * blocking call, may block current thread execution if local file changed.
     */
    val byteArray: ByteArray
}