package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/5/6 23:24
 */
enum class MsgType(val value: Int) {
    Text(1),
    Image(2),
    Audio(3),
    Video(4),
    Custom(5);
}