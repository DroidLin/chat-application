package com.chat.compose.app.util

/**
 * @author liuzhongao
 * @since 2024/7/28 22:31
 */
enum class Platform {
    MacOS,
    Windows,
    Linux,
    Unknown;
}

val currentPlatform: Platform by lazy {
    val osName = System.getProperty("os.name")
    when {
        osName.startsWith("Mac OS") -> Platform.MacOS
        osName.startsWith("Windows") -> Platform.Windows
        osName.startsWith("Linux") -> Platform.Linux
        else -> Platform.Unknown
    }
}