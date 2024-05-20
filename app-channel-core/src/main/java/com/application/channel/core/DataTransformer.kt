package com.application.channel.core

infix fun <In : Any?, Mid : Any?, Out : Any?> DataTransformDecoder<In, Mid>.next(decoder: DataTransformDecoder<Mid, Out>): DataTransformDecoder<In, Out> {
    return DataTransformDecoderWrapper(this.rawDecoderList + decoder.rawDecoderList)
}

val <In : Any?, Out : Any?> DataTransformDecoder<In, Out>.rawDecoderList: List<DataTransformDecoder<*, *>>
    get() = if (this is DataTransformDecoderWrapper<*, *>) {
        this.transformDecoder
    } else listOf(this)

private class DataTransformDecoderWrapper<In : Any?, Out : Any?>(
    val transformDecoder: List<DataTransformDecoder<*, *>>
) : DataTransformDecoder<In, Out>() {
    override fun decode(msg: In, out: MutableList<Out>) {}
}

infix fun <In : Any?, Mid : Any?, Out : Any?> DataTransformEncoder<In, Mid>.next(decoder: DataTransformEncoder<Mid, Out>): DataTransformEncoder<In, Out> {
    return DataTransformEncoderWrapper(this.rawDecoderList + decoder.rawDecoderList)
}

val <In : Any?, Out : Any?> DataTransformEncoder<In, Out>.rawDecoderList: List<DataTransformEncoder<*, *>>
    get() = if (this is DataTransformEncoderWrapper<*, *>) {
        this.transformDecoder
    } else listOf(this)

private class DataTransformEncoderWrapper<In : Any?, Out : Any?>(
    val transformDecoder: List<DataTransformEncoder<*, *>>
) : DataTransformEncoder<In, Out>() {
    override fun encode(msg: In, out: MutableList<Out>) {}
}
