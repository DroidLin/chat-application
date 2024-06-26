package com.app.channel.backend.server.di

import com.app.channel.backend.server.room.AppBackendDatabase
import com.app.channel.backend.server.room.dao.AccountDao
import com.app.channel.backend.server.room.dao.SessionInfoDao
import com.app.channel.backend.server.room.dao.UserInfoDao
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/26 23:42
 */
val DatabaseModule = module {
    single { AppBackendDatabase() }
    factory<UserInfoDao> { get<AppBackendDatabase>().userInfoDao }
    factory<SessionInfoDao> { get<AppBackendDatabase>().sessionInfoDao }
    factory<AccountDao> { get<AppBackendDatabase>().accountDao}
}