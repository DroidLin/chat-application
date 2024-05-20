package com.application.channel.core.server.dns

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.dns.*


/**
 * @author liuzhongao
 * @since 2024/5/19 22:52
 */
internal class DefaultDnsHandler(private val provider: DomainAddressProvider) : SimpleChannelInboundHandler<DnsQuery>() {

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: DnsQuery?) {
        ctx ?: return
        msg ?: return
        val question: DnsQuestion = msg.recordAt(DnsSection.QUESTION)
        ctx.writeAndFlush(newResponse(msg, question))
    }

    private fun newResponse(
        query: DnsQuery,
        question: DnsQuestion,
        ttl: Long = 10,
    ): DefaultDnsResponse {
        val response = DefaultDnsResponse(query.id())
        response.addRecord(DnsSection.QUESTION, question)

        val ipAddress = this.provider.getDomainIPAddress(domain = question.name())
        if (ipAddress != null) {
            val queryAnswer = DefaultDnsRawRecord(
                question.name(),
                DnsRecordType.A, ttl, Unpooled.wrappedBuffer(ipAddress)
            )
            response.addRecord(DnsSection.ANSWER, queryAnswer)
        }
        return response
    }
}