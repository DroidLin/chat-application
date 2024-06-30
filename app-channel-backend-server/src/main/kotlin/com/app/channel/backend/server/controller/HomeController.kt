package com.app.channel.backend.server.controller

import com.app.channel.backend.server.metadata.ApiResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author liuzhongao
 * @since 2024/6/29 02:09
 */
@RestController
class HomeController {

    @RequestMapping("/")
    suspend fun homePage() = run {
        ApiResult.success("Hello World.")
    }
}