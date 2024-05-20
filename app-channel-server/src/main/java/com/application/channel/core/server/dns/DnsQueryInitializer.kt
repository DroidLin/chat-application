package com.application.channel.core.server.dns

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.handler.codec.dns.DatagramDnsQueryDecoder
import io.netty.handler.codec.dns.DatagramDnsQueryEncoder
import io.netty.handler.codec.dns.TcpDnsQueryEncoder
import io.netty.handler.codec.dns.TcpDnsResponseDecoder

/**
 * @author liuzhongao
 * @since 2024/5/19 20:01
 */
internal class DnsQueryInitializer(private val domainProvider: DomainAddressProvider) : ChannelInitializer<NioDatagramChannel>() {
    override fun initChannel(ch: NioDatagramChannel?) {
        ch ?: return

        ch.pipeline().addLast(TcpDnsResponseDecoder())
        ch.pipeline().addLast(DatagramDnsQueryDecoder())
        ch.pipeline().addLast(DefaultDnsHandler(this.domainProvider))
        ch.pipeline().addLast(DatagramDnsQueryEncoder())
        ch.pipeline().addLast(TcpDnsQueryEncoder())

    }
}