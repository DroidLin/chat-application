package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/5/29 23:15
 */
enum class SessionType(val value: Int) {
    Unknown(0),
    P2P(1),
    Group(2);

    companion object {
        @JvmStatic
        fun fromValue(value: Int): SessionType {
            return when (value) {
                P2P.value -> P2P
                Group.value -> Group
                else -> Unknown
            }
        }
    }
}