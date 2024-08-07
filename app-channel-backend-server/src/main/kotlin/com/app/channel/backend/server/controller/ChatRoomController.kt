package com.app.channel.backend.server.controller

import com.app.channel.backend.server.CodeConst
import com.app.channel.backend.server.exceptions.BizException
import com.app.channel.backend.server.service.ChatRoomService
import com.app.channel.backend.server.util.userContext
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author liuzhongao
 * @since 2024/8/3 09:41
 */
@RestController
@RequestMapping("/chatrooms")
class ChatRoomController(private val chatRoomService: ChatRoomService) {

    @RequestMapping(
        method = [RequestMethod.GET, RequestMethod.POST],
        value = ["/create"]
    )
    suspend fun createChatRoom(
        @RequestParam(value = "chatRoomName", required = true) chatRoomName: String,
        httpServletRequest: HttpServletRequest
    ) = kotlin.run {
        if (chatRoomName.isEmpty()) throw BizException(CodeConst.CODE_INVALID_PARAMETER, "chat room name is empty.")

        val profile = httpServletRequest.userContext?.profileDTO
            ?: throw BizException(CodeConst.CODE_NOT_LOGIN, "not login.")
        val userSessionId = profile.sessionInfo?.sessionId
        val sessionType = profile.sessionInfo?.sessionTypeCode
        if (userSessionId.isNullOrEmpty() || sessionType == null) {
            throw BizException(CodeConst.CODE_INTERNAL_ERROR, "user session info invalid.")
        }

        this.chatRoomService.createChatRoom(chatRoomName, userSessionId, sessionType)
    }
}