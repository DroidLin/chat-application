package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/5/6 23:24
 */
enum class MsgType(val value: Int) {
    Undefined(-1),
    Text(1),
    Image(2),
    Audio(3),
    Video(4),
    Custom(5);

    companion object {
        @JvmStatic
        fun fromValue(value: Int): MsgType {
            return when (value) {
                Text.value -> Text
                Image.value -> Image
                Audio.value -> Audio
                Video.value -> Video
                Custom.value -> Custom
                else -> Undefined
            }
        }
    }
}