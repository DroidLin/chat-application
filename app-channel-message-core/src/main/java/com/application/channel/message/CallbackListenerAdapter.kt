package com.application.channel.message

import com.application.channel.core.Listener

/**
 * @author liuzhongao
 * @since 2024/6/1 11:29
 */
class CallbackListenerAdapter(private val callback: Callback) : Listener {
    override fun onSuccess() {
        this.callback.onSuccess()
    }

    override fun onFailure(cause: Throwable) {
        this.callback.onFailure(cause)
    }
}