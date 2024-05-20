package com.application.channel.core.server.dns

import com.application.channel.core.model.DnsServerInitConfig
import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author liuzhongao
 * @since 2024/5/19 19:31
 */
interface DnsServer {

    val initConfig: DnsServerInitConfig

    fun addDomainBinding(domain: String, ipAddress: String): Boolean

    fun removeDomainBinding(domain: String): Boolean

    fun shutDown()
}

fun DnsServer(initConfig: DnsServerInitConfig): DnsServer {
    return InternalDnsServer(initConfig)
}

private class InternalDnsServer(override val initConfig: DnsServerInitConfig) : DnsServer {

    private val logger = Logger.getLogger("InternalDnsServer")

    private val domainAddressProvider = DomainAddressProvider(this.initConfig.dnsFilePath)
    private val dnsEventBootstrap = Bootstrap()
    private val dnsEventLoop = NioEventLoopGroup(1)

    init {
        this.dnsEventBootstrap.group(this.dnsEventLoop)
            .channel(NioDatagramChannel::class.java)
            .handler(DnsQueryInitializer(this.domainAddressProvider))
            .option(ChannelOption.SO_BROADCAST, true)
            .bind(this.initConfig.port)
            .addListener { bindFuture ->
                if (bindFuture.isSuccess) {
                    this.logger.log(Level.INFO, "bind success")
                } else {
                    this.logger.log(Level.WARNING, "bind failed: ${bindFuture.cause()}")
                    bindFuture.cause()?.printStackTrace()
                }
            }
    }

    override fun addDomainBinding(domain: String, ipAddress: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeDomainBinding(domain: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun shutDown() {
        this.dnsEventLoop.shutdownGracefully()
    }
}