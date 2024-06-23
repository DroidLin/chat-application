/**
 * @author liuzhongao
 * @since 2024/5/8 22:24
 */
fun main() {
//    val initConfig = socketInitConfig {
//        address("http://127.0.0.1:8325")
//        maxReconnectCount(3)
//        afterNewValueRead { channelContext, any ->
//            println("receive data from: ${channelContext.channelRemoteAddress}, data: ${any}")
//        }
//        onExceptionCreated { _, throwable ->
//            throwable?.printStackTrace()
//        }
//        initAdapter {
//            encoderFactories {
//                listOf(
//                    ByteArrayToByteBufEncoder(),
//                    StringToByteArrayEncoder(),
//                    object : MessageToMessageEncoder<ByteArrayWritable>() {
//                        override fun encode(ctx: ChannelHandlerContext?, msg: ByteArrayWritable?, out: MutableList<Any>?) {
//                            if(ctx == null || msg == null || out == null) return
//                            out += msg.value
//                        }
//                    },
//                    object : MessageToMessageEncoder<StringWritable>() {
//                        override fun encode(ctx: ChannelHandlerContext?, msg: StringWritable?, out: MutableList<Any>?) {
//                            if(ctx == null || msg == null || out == null) return
//                            out += msg.value
//                        }
//                    }
//                )
//            }
//            decoderFactories {
//                listOf(
//                    ByteBufToByteArrayDecoder(),
//                    ByteArrayToStringDecoder(),
//                )
//            }
//        }
//    }
//    val chatClient = ChannelClient()
//    chatClient.start(initConfig) {
//        chatClient.writeValue(
//            value = StringWritable("Hello World!"),
//            channelContextMatcher = ChannelContextMatcher { channelContext ->
//                channelContext.channelRemoteAddress == initConfig.socketAddress
//            },
//            listener = SimpleWriteResultListener()
//        )
//    }
//        remoteExecutor.shutDown()
}