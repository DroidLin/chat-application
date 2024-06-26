package com.app.channel.backend.server.router

import com.app.channel.backend.server.metadata.ApiResult
import com.app.channel.backend.server.router.login.loginRouter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * @author liuzhongao
 * @since 2024/6/25 23:47
 */
fun Application.router() {
    routing {
        get("/") {
            this.call.respond(status = HttpStatusCode.OK, message = "Hello World.")
        }
        loginRouter()
    }
}