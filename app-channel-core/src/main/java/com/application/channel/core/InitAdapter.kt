package com.application.channel.core

/**
 * @author liuzhongao
 * @since 2024/5/18 18:58
 */
interface InitAdapter {

    val dataTransformEncoderFactory: () -> List<DataTransformEncoder<*, *>>?
    val dataTransformDecoderFactory: () -> List<DataTransformDecoder<*, *>>?

    val dataHandler: () -> List<DataHandler<*>>?
}

fun initAdapter(function: InitialAdapterBuilder.() -> Unit): InitAdapter {
    val builder = InitialAdapterBuilder().apply(function)
    return object : InitAdapter {
        override val dataTransformEncoderFactory: () -> List<DataTransformEncoder<*, *>>? =
            builder.dataTransformEncoderFactory
        override val dataTransformDecoderFactory: () -> List<DataTransformDecoder<*, *>>? =
            builder.dataTransformDecoderFactory

        override val dataHandler: () -> List<DataHandler<*>>? = builder.dataHandlerFactory
    }
}

@DslMarker
annotation class InitialAdapterScope

@InitialAdapterScope
class InitialAdapterBuilder {

    var dataTransformEncoderFactory: () -> List<DataTransformEncoder<*, *>>? = { null }
    var dataTransformDecoderFactory: () -> List<DataTransformDecoder<*, *>>? = { null }

    var dataHandlerFactory: () -> List<DataHandler<*>>? = { null }
}