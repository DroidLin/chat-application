package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/1 23:11
 */


actual fun ImmutableMapStorage(filePath: String, fileName: String): MapStorage {
    return JvmJSONObjectMapStorage(filePath, fileName)
}

actual fun MutableMapStorage(filePath: String, fileName: String): MutableMapStorage {
    return JvmJSONObjectMutableMapStorage(filePath, fileName)
}
