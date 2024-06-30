package com.app.channel.backend.server.config

import com.app.channel.backend.server.interceptor.UserAuthorizationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author liuzhongao
 * @since 2024/6/29 02:21
 */
@Configuration
class WebConfig : WebMvcConfigurer {

    @Autowired
    private lateinit var userAuthorizationInterceptor: UserAuthorizationInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(userAuthorizationInterceptor)
            .excludePathPatterns("/api/account/*")
    }
}