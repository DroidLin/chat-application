package com.application.channel.message.session

import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/5/30 22:15
 */
interface ChannelInfo {

    val sessionId: String

    val sessionType: SessionType

    val updateTime: Long

    val createTime: Long
}