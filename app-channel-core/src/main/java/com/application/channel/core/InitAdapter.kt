package com.application.channel.core

import io.netty.channel.ChannelHandler
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.handler.codec.MessageToMessageEncoder

/**
 * @author liuzhongao
 * @since 2024/5/18 18:58
 */
interface InitAdapter {

    val dataTransformEncoderFactory: Factory<List<MessageToMessageEncoder<*>>?>
    val dataTransformDecoderFactory: Factory<List<MessageToMessageDecoder<*>>?>

    val dataHandler: Factory<List<ChannelHandler>?>
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

    private var dataTransformEncoderFactory: Factory<List<MessageToMessageEncoder<*>>?> = Factory { null }
    private var dataTransformDecoderFactory: Factory<List<MessageToMessageDecoder<*>>?> = Factory { null }

    private var dataHandlerFactory: Factory<List<ChannelHandler>?> = Factory { null }

    fun encoderFactories(function: Factory<List<MessageToMessageEncoder<*>>?>) {
        this.dataTransformEncoderFactory = function
    }

    fun decoderFactories(function: Factory<List<MessageToMessageDecoder<*>>?>) {
        this.dataTransformDecoderFactory = function
    }

    fun handlerFactories(function: Factory<List<ChannelHandler>?>) {
        this.dataHandlerFactory = function
    }

    fun build(): InitAdapter {
        return object : InitAdapter {
            override val dataTransformEncoderFactory: Factory<List<MessageToMessageEncoder<*>>?> =
                this@InitialAdapterBuilder.dataTransformEncoderFactory
            override val dataTransformDecoderFactory: Factory<List<MessageToMessageDecoder<*>>?> =
                this@InitialAdapterBuilder.dataTransformDecoderFactory

            override val dataHandler: Factory<List<ChannelHandler>?> = this@InitialAdapterBuilder.dataHandlerFactory
        }
    }
}