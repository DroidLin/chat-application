package com.application.channel.core.util

import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools

/**
 * @author liuzhongao
 * @since 2024/6/23 12:34
 */


inline fun <reified T> koinInject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    return GlobalContext.get().get(qualifier, parameters)
}

inline fun <reified T> koinInjectLazy(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> {
    return GlobalContext.get().inject(qualifier, mode, parameters)
}