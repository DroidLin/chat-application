package com.app.channel.backend.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author liuzhongao
 * @since 2024/6/25 23:44
 */

@SpringBootApplication
@EnableScheduling
class SpringServerApplication

fun main(args: Array<String>) {
    runApplication<SpringServerApplication>(*args)
}