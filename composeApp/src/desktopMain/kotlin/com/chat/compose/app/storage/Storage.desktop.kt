package com.chat.compose.app.storage

import com.android.dependencies.common.ComponentFacade
import com.android.dependencies.storage.JvmStorageType
import com.android.dependencies.storage.MutableMapStorage
import com.android.dependencies.storage.StorageCenter

/**
 * @author liuzhongao
 * @since 2024/6/23 23:32
 */
actual fun MutableMapStorage(fileName: String): MutableMapStorage {
    return ComponentFacade[StorageCenter::class.java].newMutableMapStorage(
        storageName = fileName,
        storageType = JvmStorageType.JSON,
        directory = "preference/"
    )
}