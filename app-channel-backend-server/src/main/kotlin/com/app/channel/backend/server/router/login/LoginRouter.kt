package com.app.channel.backend.server.router.login

import com.app.channel.backend.server.di.koinInject
import io.ktor.server.routing.*

/**
 * @author liuzhongao
 * @since 2024/6/26 22:13
 */
fun Routing.loginRouter() {
    koinInject<LoginService>().run { route() }
}