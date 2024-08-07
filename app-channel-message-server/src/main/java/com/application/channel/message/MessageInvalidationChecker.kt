package com.application.channel.message

import com.application.channel.message.meta.Message
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/8/4 11:31
 */
interface MessageInvalidationChecker {

    fun messageInvalidation(context: Context, message: Message): Boolean
}

internal class DefaultMessageInvalidationChecker @Inject constructor(): MessageInvalidationChecker {
    override fun messageInvalidation(context: Context, message: Message): Boolean = false
}