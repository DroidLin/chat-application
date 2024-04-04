package com.android.dependencies.storage

/**
 * @author liuzhongao
 * @since 2024/4/1 23:02
 */

expect fun ImmutableMapStorage(filePath: String, fileName: String): MapStorage

expect fun MutableMapStorage(filePath: String, fileName: String): MutableMapStorage