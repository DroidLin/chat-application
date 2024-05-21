package com.application.channel.core.client

import com.application.channel.core.model.SimpleSocketInitConfig

/**
 * @author liuzhongao
 * @since 2024/5/21 23:14
 */
interface ConnectListener {

    fun onConnected(initConfig: SimpleSocketInitConfig) {}

    fun onFailure(throwable: Throwable?) {}
}