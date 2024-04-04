package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/2 22:07
 */

actual fun ImmutableMapStorage(filePath: String, fileName: String): MapStorage {
    return SharedPreferenceMapStorage(fileName)
}

actual fun MutableMapStorage(filePath: String, fileName: String): MutableMapStorage {
    return SharedPreferenceMutableMapStorage(fileName)
}
