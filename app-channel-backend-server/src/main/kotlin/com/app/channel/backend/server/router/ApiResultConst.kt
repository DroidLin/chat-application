package com.app.channel.backend.server.router

import com.app.channel.backend.server.metadata.ApiResult
import com.app.channel.backend.server.router.CodeConst.Companion.CODE_INTERNAL_ERROR
import com.app.channel.backend.server.router.CodeConst.Companion.CODE_INVALID_PARAMETER
import com.app.channel.backend.server.router.CodeConst.Companion.CODE_PASSWORD_ERROR_OR_USER_NOT_EXIST
import com.app.channel.backend.server.router.CodeConst.Companion.CODE_USER_NOT_FOUNT

/**
 * @author liuzhongao
 * @since 2024/6/27 00:48
 */
interface CodeConst {
    companion object {
        const val CODE_USER_NOT_FOUNT = -101
        const val CODE_INVALID_PARAMETER = -102
        const val CODE_PASSWORD_ERROR_OR_USER_NOT_EXIST = -103
        const val CODE_INTERNAL_ERROR = -104
    }
}

val USER_NOT_FOUNT = ApiResult.failure(code = CODE_USER_NOT_FOUNT, message = "用户不存在")
val INVALID_PARAMETER = ApiResult.failure(code = CODE_INVALID_PARAMETER, message = "非法参数")
val PASSWORD_ERROR_OR_USER_NOT_EXIST = ApiResult.failure(code = CODE_PASSWORD_ERROR_OR_USER_NOT_EXIST, message = "非法参数")
val INTERNAL_ERROR = ApiResult.failure(code = CODE_INTERNAL_ERROR, message = "非法参数")