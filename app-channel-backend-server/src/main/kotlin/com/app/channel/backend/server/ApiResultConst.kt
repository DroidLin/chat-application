package com.app.channel.backend.server

/**
 * @author liuzhongao
 * @since 2024/6/27 00:48
 */
interface CodeConst {
    companion object {
        const val CODE_USER_NOT_FOUND = -101
        const val CODE_INVALID_PARAMETER = -102
        const val CODE_PASSWORD_ERROR_OR_USER_NOT_EXIST = -103
        const val CODE_INTERNAL_ERROR = -104
        const val CODE_NOT_LOGIN = -105
        const val CODE_USER_ALREADY_EXIST = -106
        const val CODE_USER_ACCOUNT_IS_USED = -107
    }
}