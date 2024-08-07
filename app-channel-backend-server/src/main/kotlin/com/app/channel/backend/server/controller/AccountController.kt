package com.app.channel.backend.server.controller

import com.app.channel.backend.server.CodeConst
import com.app.channel.backend.server.exceptions.BizException
import com.app.channel.backend.server.metadata.ApiResult
import com.app.channel.backend.server.metadata.ProfileDTO
import com.app.channel.backend.server.service.BasicUserService
import com.app.channel.backend.server.util.hostInRequestUrl
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Base64

/**
 * @author liuzhongao
 * @since 2024/6/29 01:53
 */
@RestController
@RequestMapping("/api/account")
class AccountController(
    private val basicUserService: BasicUserService,
    private val objectMapper: ObjectMapper
) {

    @RequestMapping(
        value = ["/login"],
        method = [RequestMethod.GET, RequestMethod.POST]
    )
    suspend fun accountLogin(
        @RequestParam(value = "userAccount") userAccount: String?,
        @RequestParam(value = "password") passwordHash: String?,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ApiResult<ProfileDTO> {
        val userInfoVO = this.basicUserService.checkAndLogin(userAccount, passwordHash)
        val userInfoDetail = this.objectMapper.writeValueAsString(userInfoVO)
        val base64EncodeResult = Base64.getEncoder().encodeToString(userInfoDetail.encodeToByteArray())
        val cookie = Cookie("user_token", base64EncodeResult)
        cookie.maxAge = 60 * 60 * 24 * 30
        cookie.domain = request.hostInRequestUrl
        cookie.path = "/"
        response.addCookie(cookie)
        return ApiResult.success(userInfoVO)
    }

    @RequestMapping(
        value = ["/registration"],
        method = [RequestMethod.GET, RequestMethod.POST]
    )
    suspend fun accountRegistration(
        @RequestParam(value = "userAccount") userAccount: String?,
        @RequestParam(value = "userName") userName: String?,
        @RequestParam(value = "password") passwordHash: String?,
    ): ApiResult<out Any> {
        this.basicUserService.createUserAccount(userName, userAccount, passwordHash)
        return ApiResult.success(null)
    }

    @RequestMapping(
        value = ["/user/check"],
        method = [RequestMethod.GET, RequestMethod.POST]
    )
    suspend fun userAccountCheck(
        @RequestParam(value = "userAccount") userAccount: String?,
    ): ApiResult<out Any> {
        val accountExist = this.basicUserService.checkUserAccountExist(userAccount)
        if (accountExist) return ApiResult.success(null)
        throw BizException(code = CodeConst.CODE_USER_ACCOUNT_IS_USED, message = "account is used")
    }
}