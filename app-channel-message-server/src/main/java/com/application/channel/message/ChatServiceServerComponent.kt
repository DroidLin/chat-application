package com.application.channel.message

import com.application.channel.core.server.ChannelServerComponent
import com.application.channel.message.controller.Controller
import com.application.channel.message.controller.HostController
import com.application.channel.message.meta.Message
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import java.util.logging.Logger
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/1 01:36
 */
@Singleton
@Component(
    modules = [
        CommonServerModule::class,
        CommonServerBindingModule::class,
        KeyModule::class,
        MessageParserModule::class
    ],
    dependencies = [
        ChannelServerComponent::class
    ]
)
interface ChatServiceServerComponent {

    fun chatService(): ChatService
}

@Module
internal class CommonServerModule {

    @Provides
    fun logger(): Logger = Logger.getLogger("CommonServer")
}

@Module
internal interface CommonServerBindingModule {

    @Binds
    fun provideChatService(impl: ChatServiceImpl): ChatService

    @Singleton
    @Binds
    fun provideAuthorizationMapping(impl: SessionContextAuthorizationMapping): AuthorizationMapping

    @Binds
    fun provideMessageController(wrapper: HostController): Controller<Message>
}