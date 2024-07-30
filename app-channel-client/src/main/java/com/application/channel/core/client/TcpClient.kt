package com.application.channel.core.client

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.InitAdapter
import com.application.channel.core.Listener
import com.application.channel.core.WriteToChannelJob
import com.application.channel.core.handler.codc.ChannelCollectorCodec
import com.application.channel.core.model.Writable
import com.application.channel.core.model.channelContext
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.TimeUnit

/**
 * @author liuzhongao
 * @since 2024/6/23 02:30
 */
class TcpClient {

    private var channelGroup: ChannelGroup? = null
    private var eventLoop: NioEventLoopGroup? = null

    private var runningChannel: ChannelFuture? = null

    fun startService(tcpInitConfig: TcpLocalInitConfig) {
        val channelGroup: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
        val eventLoop = NioEventLoopGroup(4)
        val channelInitializer = TcpChannelInitializer(
            channelGroup = channelGroup,
            initAdapter = tcpInitConfig.initAdapter,
            observer = tcpInitConfig.observer
        )
        val channelFutureListener = ChannelFutureListener { future ->
            val channelContext = future.channel().channelContext
            val observer = tcpInitConfig.observer
            if (!future.isSuccess) {
                observer.onConnectionFailure(channelContext, future.cause())
            } else observer.onConnectionSuccess(channelContext)
        }
        this.runningChannel?.channel()?.close()?.awaitUninterruptibly()
        this.runningChannel = Bootstrap()
            .channel(NioSocketChannel::class.java)
            .handler(channelInitializer)
            .group(eventLoop)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_SNDBUF, 4096)
            .option(ChannelOption.SO_RCVBUF, 4096)
            .option(ChannelOption.AUTO_CLOSE, false)
            .option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark(8 * 1024, 32 * 1024))
            .connect(tcpInitConfig.socketAddress)
            .addListener(channelFutureListener)
        this.channelGroup = channelGroup
        this.eventLoop = eventLoop
    }

    @JvmOverloads
    fun scheduleInEventLoop(delay: Long = 0, function: () -> Unit) {
        this.eventLoop?.schedule(function, delay, TimeUnit.MILLISECONDS)
    }

    @JvmOverloads
    fun write(
        writable: Writable,
        contextMatcher: ChannelContextMatcher = ChannelContextMatcher.all,
        listener: Listener? = null
    ) {
        val channelGroup = this.channelGroup ?: return
        val writeToChannelJob = WriteToChannelJob(
            channelGroup = channelGroup,
            value = writable,
            channelMatcher = contextMatcher,
            listener = listener
        )
        this.scheduleInEventLoop(function = writeToChannelJob::run)
    }

    fun stopService() {
        this.channelGroup?.close()?.sync()
        this.channelGroup?.clear()
        this.channelGroup = null
        this.runningChannel?.channel()?.closeFuture()?.sync()
        this.runningChannel = null
        this.eventLoop?.shutdownGracefully()
        this.eventLoop = null
    }
}

private class TcpChannelInitializer(
    private val channelGroup: ChannelGroup,
    private val initAdapter: InitAdapter?,
    private val observer: TcpConnectionObserver?
) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline() ?: return
        pipeline.addLast("channelCollector", ChannelCollectorCodec(this.channelGroup))
        pipeline.addLast("handler", TcpChannelActivityAdapter(this.observer))

        // encoder only

        // decoder only

        // custom
        this.initAdapter?.dataTransformDecoderFactory
            ?.instantiate()
            ?.also { decoders -> pipeline.addLast(*decoders.toTypedArray()) }

        this.initAdapter?.dataTransformEncoderFactory
            ?.instantiate()
            ?.also { encoders -> pipeline.addLast(*encoders.toTypedArray()) }

        this.initAdapter?.dataHandler
            ?.instantiate()
            ?.also { handlers -> pipeline.addLast(*handlers.toTypedArray()) }
    }
}

@Sharable
private class TcpConnectionNotifyAdapter : SimpleChannelInboundHandler<Any>() {
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: Any?) {
    }
}