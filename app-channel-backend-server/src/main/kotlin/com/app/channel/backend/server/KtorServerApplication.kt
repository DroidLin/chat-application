package com.app.channel.backend.server

import com.app.channel.backend.server.di.DatabaseModule
import com.app.channel.backend.server.di.LoginModule
import com.app.channel.backend.server.router.router
import com.fasterxml.jackson.core.util.JacksonFeature
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.core.context.startKoin

/**
 * @author liuzhongao
 * @since 2024/6/25 23:44
 */
fun main() {
    startKoin {
        modules(DatabaseModule, LoginModule)
    }
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::init)
        .start(wait = true)
}

private fun Application.init() {
    interceptor()
    router()
    jacksonInit()
}

private fun Application.jacksonInit() {
    install(ContentNegotiation) {
        jackson()
    }
}