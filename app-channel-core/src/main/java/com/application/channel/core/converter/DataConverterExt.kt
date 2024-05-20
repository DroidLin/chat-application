package com.application.channel.core.converter

/**
 * @author liuzhongao
 * @since 2024/5/18 18:44
 */
infix fun <In : Any?, Mid : Any?, Out : Any?> DataConverter<In, Mid>.and(dataConverter: DataConverter<Mid, Out>): DataConverter<In, Out> {
    return object : DataConverter<In, Out> {
        override fun convert(value: In): Out = dataConverter.convert(value = this@and.convert(value))
    }
}