package com.application.channel.core

/**
 * @author liuzhongao
 * @since 2024/5/12 11:44
 */
interface Listener {

    fun onSuccess()

    fun onFailure(cause: Throwable)
}