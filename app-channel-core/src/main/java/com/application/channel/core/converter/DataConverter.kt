package com.application.channel.core.converter

/**
 * @author liuzhongao
 * @since 2024/5/18 18:19
 */
fun interface DataConverter<In : Any?, Out : Any?> {

    fun convert(value: In): Out
}
