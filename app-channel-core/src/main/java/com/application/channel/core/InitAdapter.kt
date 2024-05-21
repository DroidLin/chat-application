package com.application.channel.core

/**
 * @author liuzhongao
 * @since 2024/5/18 18:58
 */
interface InitAdapter {

    val dataTransformEncoderFactory: Factory<List<DataTransformEncoder<*, *>>?>
    val dataTransformDecoderFactory: Factory<List<DataTransformDecoder<*, *>>?>

    val dataHandler: Factory<List<DataHandler<*>>?>
}

fun initAdapter(function: InitialAdapterBuilder.() -> Unit): InitAdapter {
    val builder = InitialAdapterBuilder().apply(function)
    return object : InitAdapter {
        override val dataTransformEncoderFactory: Factory<List<DataTransformEncoder<*, *>>?> =
            builder.dataTransformEncoderFactory
        override val dataTransformDecoderFactory: Factory<List<DataTransformDecoder<*, *>>?> =
            builder.dataTransformDecoderFactory

        override val dataHandler: Factory<List<DataHandler<*>>?> = builder.dataHandlerFactory
    }
}

fun interface Factory<T> {
    fun instantiate(): T
}

@DslMarker
annotation class InitialAdapterScope

@InitialAdapterScope
class InitialAdapterBuilder {

    var dataTransformEncoderFactory: Factory<List<DataTransformEncoder<*, *>>?> = Factory { null }
    var dataTransformDecoderFactory: Factory<List<DataTransformDecoder<*, *>>?> = Factory { null }

    var dataHandlerFactory: Factory<List<DataHandler<*>>?> = Factory { null }
}