package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/6/23 20:43
 */
enum class FailureType(val value: Int) {
    Undefined(0),
    LoginConnectFailure(1),
    ;


    companion object {
        @JvmStatic
        fun fromValue(value: Int): FailureType {
            return when (value) {
                1 -> LoginConnectFailure
                else -> Undefined
            }
        }
    }
}

enum class LogoutReason(val value: Int) {
    Undefined(0),
    LogoutUserTrigger(1),

    ;

    companion object {
        @JvmStatic
        fun fromValue(value: Int): LogoutReason {
            return when (value) {
                1 -> LogoutUserTrigger
                else -> Undefined
            }
        }
    }
}