package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/3/27 23:21
 */
interface Storage {

    val absolutePath: String

    val storageName: String

    fun interface Factory {

        fun storageCreate(directoryPath: String, storageName: String, storageType: StorageType): Storage
    }
}