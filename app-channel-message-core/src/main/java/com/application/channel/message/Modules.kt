package com.application.channel.message

import com.application.channel.message.meta.*
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/2 09:36
 */
@Module
class KeyModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class EncryptReleaseKey

    @EncryptReleaseKey
    @Provides
    fun provideKey(): ByteArray {
        return "LZA".encodeToByteArray()
    }
}

@Module
class MessageParserModule(private val additionalParserList: List<MessageParser>) {

    constructor(): this(listOf())

    @Singleton
    @Provides
    fun provideCommonParserList(): MessageParser {
        val parserList = listOf(
            *additionalParserList.toTypedArray(),
            TextMessage.Parser(),
            ImageMessage.Parser(),
            LoginMessage.Parser(),
            LoginResultMessage.Parser(),
            MessageImpl.Parser(),
        )
        return MessageParser(parserList)
    }
}