package com.application.channel.message

import dagger.Binds
import dagger.Component
import dagger.Module

/**
 * @author liuzhongao
 * @since 2024/8/4 22:13
 */

@Component(
    modules = [
        SessionOptimizerModule::class
    ]
)
interface SessionOptimizeComponent {

    fun relationOptimizer(): SessionRelationOptimizer

    fun messageInvalidationChecker(): MessageInvalidationChecker
}

@Module
internal interface SessionOptimizerModule {

    @Binds
    fun provideInvalidationChecker(impl: DefaultMessageInvalidationChecker): MessageInvalidationChecker

    @Binds
    fun provideRelationOptimizer(impl: DefaultSessionRelationOptimizer): SessionRelationOptimizer
}