package com.application.channel.message.modules

import com.application.channel.database.AppMessageDatabase
import dagger.Module
import dagger.Provides

/**
 * @author liuzhongao
 * @since 2024/6/8 21:08
 */
@Module
class MessageDatabaseModule constructor(private val database: AppMessageDatabase?) {

    constructor() : this(null)

    @Provides
    fun provideAppDatabase(): AppMessageDatabase? = this.database
}