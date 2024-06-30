package com.app.channel.backend.server.component

import com.app.channel.backend.server.room.AppBackendDatabase
import com.app.channel.backend.server.room.dao.AccountDao
import com.app.channel.backend.server.room.dao.SessionInfoDao
import com.app.channel.backend.server.room.dao.UserInfoDao
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

/**
 * @author liuzhongao
 * @since 2024/6/29 02:29
 */
@Component
class DatabaseComponent {

    @Bean
    fun appBackendDatabase(): AppBackendDatabase {
        return AppBackendDatabase()
    }

    @Bean
    fun userInfoDao(appBackendDatabase: AppBackendDatabase): UserInfoDao = appBackendDatabase.userInfoDao

    @Bean
    fun sessionInfoDao(appBackendDatabase: AppBackendDatabase): SessionInfoDao = appBackendDatabase.sessionInfoDao

    @Bean
    fun accountDao(appBackendDatabase: AppBackendDatabase): AccountDao = appBackendDatabase.accountDao
}

