package com.app.channel.backend.server.controller

import com.app.channel.backend.server.CodeConst
import com.app.channel.backend.server.exceptions.BizException
import com.app.channel.backend.server.metadata.ApiResult
import com.app.channel.backend.server.service.UserService
import com.app.channel.backend.server.util.userContext
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author liuzhongao
 * @since 2024/6/29 12:43
 */
@RestController
@RequestMapping("/api/userinfo")
class UserInfoController(
    private val userService: UserService,
) {

    @RequestMapping(
        value = ["/get"],
        method = [RequestMethod.GET, RequestMethod.POST],
    )
    suspend fun getUserInfo(
        @RequestParam(value = "userId", required = false) userId: Long?,
        @RequestParam(value = "sessionId", required = false) sessionId: String?,
        httpServletRequest: HttpServletRequest
    ): ApiResult<Any> {
        if (userId != null) {
            val userInfo = this.userService.getUserInfo(userId)
            return ApiResult.success(userInfo)
        }
        if (sessionId != null) {
            val userInfo = userService.getUserInfoBySessionId(sessionId)
            return ApiResult.success(userInfo)
        }
        val selfUserId = httpServletRequest.userContext?.profileDTO?.userInfo?.userId
        if (selfUserId != null) {
            val userInfo = this.userService.getUserInfo(selfUserId)
            return ApiResult.success(userInfo)
        }
        val selfSessionId = httpServletRequest.userContext?.profileDTO?.sessionInfo?.sessionId
        if (selfSessionId != null) {
            val userInfo = this.userService.getUserInfoBySessionId(selfSessionId)
            return ApiResult.success(userInfo)
        }
        throw BizException(code = CodeConst.CODE_INVALID_PARAMETER, message = "invalid parameters.")
    }

    @RequestMapping(
        value = ["/get/batch"],
        method = [RequestMethod.GET, RequestMethod.POST],
    )
    suspend fun getUserInfo(
        @RequestParam(value = "userIdList", required = false) userIdList: List<String>?,
        @RequestParam(value = "sessionIdList", required = false) sessionIdList: List<String>?
    ): ApiResult<Any> {
        if (!userIdList.isNullOrEmpty()) {
            val userInfoList = this.userService.getUserInfo(userIdList)
            return ApiResult.success(userInfoList)
        }
        if (!sessionIdList.isNullOrEmpty()) {
            val userInfoList = this.userService.getUserInfoBySessionId(sessionIdList)
            return ApiResult.success(userInfoList)
        }
        throw BizException(code = CodeConst.CODE_INVALID_PARAMETER, message = "invalid parameters.")
    }
}