package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/5/30 22:59
 */

interface Callback {

    fun onSuccess()

    fun onFailure(throwable: Throwable)

    companion object : Callback {
        override fun onSuccess() {}
        override fun onFailure(throwable: Throwable) {}
    }
}