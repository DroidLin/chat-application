package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/6/2 10:22
 */
data class ChatServiceInitConfig(
    val tcpAddress: String,
    val encryptionKey: ByteArray,
    val account: Account,
    val messageReceiveListener: MessageReceiveListener,
    val chatServiceEventObserver: ChatServiceEventObserver?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatServiceInitConfig

        if (tcpAddress != other.tcpAddress) return false
        if (!encryptionKey.contentEquals(other.encryptionKey)) return false
        if (messageReceiveListener != other.messageReceiveListener) return false
        if (chatServiceEventObserver != other.chatServiceEventObserver) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tcpAddress.hashCode()
        result = 31 * result + encryptionKey.contentHashCode()
        result = 31 * result + messageReceiveListener.hashCode()
        result = 31 * result + (chatServiceEventObserver?.hashCode() ?: 0)
        return result
    }
}
