package com.app.channel.backend.server.util

import com.app.channel.backend.server.metadata.UserContext
import jakarta.servlet.http.HttpServletRequest
import java.net.URI

/**
 * @author liuzhongao
 * @since 2024/6/29 12:56
 */
var HttpServletRequest.userContext: UserContext?
    set(value) { this.setAttribute("userContext", value) }
    get() = this.getAttribute("userContext") as? UserContext

val HttpServletRequest.hostInRequestUrl: String
    get() {
        val requestURI = kotlin.runCatching {
            URI.create(this.requestURL.toString())
        }.onFailure { it.printStackTrace() }.getOrNull() ?:return "*"
        return requestURI.host
    }