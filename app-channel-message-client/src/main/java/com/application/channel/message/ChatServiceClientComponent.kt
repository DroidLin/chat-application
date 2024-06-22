package com.application.channel.message

import com.application.channel.core.client.ChannelClientComponent
import com.application.channel.message.modules.DBProviderModule
import com.application.channel.message.modules.InitialConfigModule
import com.application.channel.message.modules.MessageDatabaseModule
import com.application.channel.message.modules.MessageRepositoryModule
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import java.util.logging.Logger
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/1 15:57
 */
@Singleton
@Component(
    modules = [
        CommonClientModule::class,
        CommonClientBindingModule::class,
        KeyModule::class,
        MessageParserModule::class,
        MessageDatabaseModule::class,
        DBProviderModule::class,
        MessageRepositoryModule::class,
        InitialConfigModule::class,
    ],
    dependencies = [ChannelClientComponent::class]
)
interface ChatServiceClientComponent {

    fun chatService(): ChatService
}

@Module
internal class CommonClientModule {

    @Singleton
    @Provides
    fun logger(): Logger = Logger.getLogger("CommonClient")
}

@Module
internal interface CommonClientBindingModule {

    @Singleton
    @Binds
    fun chatService(impl: ChatServiceImpl): ChatService

    @Singleton
    @Binds
    fun authorization(impl: AuthorizationImpl): Authorization
}