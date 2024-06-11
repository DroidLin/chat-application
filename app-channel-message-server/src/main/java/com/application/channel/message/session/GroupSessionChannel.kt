package com.application.channel.message.session

/**
 * @author liuzhongao
 * @since 2024/5/30 22:31
 */
interface GroupSessionChannel : SessionChannel {

    fun addMember(sessionId: String)

    fun addMember(sessionChannel: SessionChannel)

    fun removeMember(sessionId: String)

    fun removeMember(sessionChannel: SessionChannel)
}