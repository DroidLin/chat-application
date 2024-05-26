package com.application.channel.core

/**
 * @author liuzhongao
 * @since 2024/5/18 18:58
 */
interface InitAdapter {

    val dataTransformEncoderFactory: Factory<Array<DataTransformEncoder<*, *>>?>
    val dataTransformDecoderFactory: Factory<Array<DataTransformDecoder<*, *>>?>

    val dataHandler: Factory<Array<DataHandler<*>>?>
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

    private var dataTransformEncoderFactory: Factory<Array<DataTransformEncoder<*, *>>?> = Factory { null }
    private var dataTransformDecoderFactory: Factory<Array<DataTransformDecoder<*, *>>?> = Factory { null }

    private var dataHandlerFactory: Factory<Array<DataHandler<*>>?> = Factory { null }

    fun encoderFactories(vararg encoder: DataTransformEncoder<*, *>) {
        this.dataTransformEncoderFactory = Factory { encoder as Array<DataTransformEncoder<*, *>>? }
    }

    fun decoderFactories(vararg decoder: DataTransformDecoder<*, *>) {
        this.dataTransformDecoderFactory = Factory { decoder as Array<DataTransformDecoder<*, *>>? }
    }

    fun handlerFactories(vararg handler: DataHandler<*>) {
        this.dataHandlerFactory = Factory { handler as Array<DataHandler<*>>? }
    }

    fun build(): InitAdapter {
        return object : InitAdapter {
            override val dataTransformEncoderFactory: Factory<Array<DataTransformEncoder<*, *>>?> =
                this@InitialAdapterBuilder.dataTransformEncoderFactory
            override val dataTransformDecoderFactory: Factory<Array<DataTransformDecoder<*, *>>?> =
                this@InitialAdapterBuilder.dataTransformDecoderFactory

            override val dataHandler: Factory<Array<DataHandler<*>>?> = this@InitialAdapterBuilder.dataHandlerFactory
        }
    }
}