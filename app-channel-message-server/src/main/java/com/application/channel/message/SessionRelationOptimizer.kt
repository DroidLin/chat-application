package com.application.channel.message

import com.application.channel.message.meta.Message
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/8/4 21:56
 */
interface SessionRelationOptimizer {

    fun optimize(message: Message): List<RelationMeta>
}

internal class DefaultSessionRelationOptimizer @Inject constructor(): SessionRelationOptimizer {
    override fun optimize(message: Message): List<RelationMeta> = emptyList()
}