package com.application.channel.core

import io.netty.channel.ChannelHandler
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.handler.codec.MessageToMessageEncoder

/**
 * @author liuzhongao
 * @since 2024/5/18 18:58
 */
interface InitAdapter {

    val dataTransformEncoderFactory: Factory<Array<MessageToMessageEncoder<*>>?>
    val dataTransformDecoderFactory: Factory<Array<MessageToMessageDecoder<*>>?>

    val dataHandler: Factory<Array<ChannelHandler>?>
}

fun initAdapter(function: InitialAdapterBuilder.() -> Unit): InitAdapter {
    return InitialAdapterBuilder().apply(function).build()
}

fun interface Factory<T> {
    fun instantiate(): T
}

@DslMarker
annotation class InitialAdapterScope

@InitialAdapterScope
class InitialAdapterBuilder internal constructor() {

    private var dataTransformEncoderFactory: Factory<Array<MessageToMessageEncoder<*>>?> = Factory { null }
    private var dataTransformDecoderFactory: Factory<Array<MessageToMessageDecoder<*>>?> = Factory { null }

    private var dataHandlerFactory: Factory<Array<ChannelHandler>?> = Factory { null }

    fun encoderFactories(vararg encoder: MessageToMessageEncoder<*>) {
        this.dataTransformEncoderFactory = Factory { encoder as Array<MessageToMessageEncoder<*>>? }
    }

    fun decoderFactories(vararg decoder: MessageToMessageDecoder<*>) {
        this.dataTransformDecoderFactory = Factory { decoder as Array<MessageToMessageDecoder<*>>? }
    }

    fun handlerFactories(vararg handler: ChannelHandler) {
        this.dataHandlerFactory = Factory { handler as Array<ChannelHandler>? }
    }

    fun build(): InitAdapter {
        return object : InitAdapter {
            override val dataTransformEncoderFactory: Factory<Array<MessageToMessageEncoder<*>>?> =
                this@InitialAdapterBuilder.dataTransformEncoderFactory
            override val dataTransformDecoderFactory: Factory<Array<MessageToMessageDecoder<*>>?> =
                this@InitialAdapterBuilder.dataTransformDecoderFactory

            override val dataHandler: Factory<Array<ChannelHandler>?> = this@InitialAdapterBuilder.dataHandlerFactory
        }
    }
}