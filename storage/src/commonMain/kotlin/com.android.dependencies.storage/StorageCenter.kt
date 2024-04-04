package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/3/27 23:20
 */
interface StorageCenter {

    fun newStorage(directoryPath: String, storageName: String, storageType: StorageType): Storage

    fun newMapStorage(directoryPath: String, storageName: String): MapStorage

    fun newFileStorage(directoryPath: String, storageName: String): FileStorage
}