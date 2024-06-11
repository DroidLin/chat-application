package com.application.channel.message.modules

import com.application.channel.message.database.DBProvider
import com.application.channel.message.database.DBProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/9 11:18
 */
@Module
internal interface DBProviderModule {

    @Singleton
    @Binds
    fun provideDBProvider(impl: DBProviderImpl): DBProvider
}