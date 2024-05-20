package com.application.channel.core.model

/**
 * @author liuzhongao
 * @since 2024/5/19 19:31
 */
data class DnsServerInitConfig(
    val port: Int,
    val dnsFilePath: String
) : InitConfig