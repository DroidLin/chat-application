package com.application.channel.message.session

/**
 * @author liuzhongao
 * @since 2024/5/30 22:31
 */
interface RoomSessionChannel : SessionChannel {

    fun addMember(sessionChannel: SessionChannel)

    fun removeMember(sessionChannel: SessionChannel)
}