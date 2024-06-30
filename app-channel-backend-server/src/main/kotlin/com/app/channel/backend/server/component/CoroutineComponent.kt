package com.app.channel.backend.server.component

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

/**
 * @author liuzhongao
 * @since 2024/6/29 13:47
 */
@Component
class CoroutineComponent {

    @Bean
    fun defaultDispatcherCoroutineScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.Default)
    }
}