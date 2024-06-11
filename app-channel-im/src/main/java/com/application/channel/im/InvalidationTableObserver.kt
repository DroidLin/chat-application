package com.application.channel.im

import com.application.channel.database.OnTableChangedObserver
import com.application.channel.message.MessageRepository
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author liuzhongao
 * @since 2024/6/12 00:29
 */
class InvalidationTableObserver(
    override val tables: List<String>,
    private val onInvalidation: () -> Unit,
) : OnTableChangedObserver {

    private val registered = AtomicBoolean(false)

    override fun onTableChanged(tableNames: Set<String>) {
        this.onInvalidation()
    }

    fun registerIfNecessary(messageRepository: MessageRepository) {
        if (this.registered.compareAndSet(false, true)) {
            messageRepository.addObserver(this)
        }
    }
}