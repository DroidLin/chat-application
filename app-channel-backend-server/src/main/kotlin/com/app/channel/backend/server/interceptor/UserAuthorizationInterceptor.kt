package com.app.channel.backend.server.interceptor

import com.app.channel.backend.server.CodeConst
import com.app.channel.backend.server.exceptions.BizException
import com.app.channel.backend.server.metadata.ProfileDTO
import com.app.channel.backend.server.metadata.UserContext
import com.app.channel.backend.server.util.userContext
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.Base64

/**
 * @author liuzhongao
 * @since 2024/6/29 02:18
 */
@Component
class UserAuthorizationInterceptor(
    private val objectMapper: ObjectMapper
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.userContext != null) {
            return true
        }
        val profileString = request.cookies?.find { it.name == "user_token" }?.value?.toString()
        if (profileString.isNullOrEmpty()) {
            throw BizException(code = CodeConst.CODE_NOT_LOGIN, message = "user not login.")
        }
        val profileDecodeString = Base64.getDecoder().decode(profileString).decodeToString()
        val profileDTO = kotlin.runCatching {
            this.objectMapper.readValue(profileDecodeString, ProfileDTO::class.java)
        }.onFailure { it.printStackTrace() }.getOrNull()
        if (profileDTO == null) {
            throw BizException(code = CodeConst.CODE_NOT_LOGIN, message = "user not login.")
        }
        request.userContext = UserContext(profileDTO)
        return true
    }

}