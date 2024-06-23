package com.application.channel.im

import com.android.dependencies.common.Component
import com.android.dependencies.common.ComponentInstaller

/**
 * @author liuzhongao
 * @since 2024/6/16 21:25
 */
class MessageComponent : Component {

    override val name: String get() = "MessageComponent"

    override fun collect(installer: ComponentInstaller) {
        installer.installLazily(MsgService::class.java) {
            SingleIMManager.msgService
        }
        installer.installLazily(MsgConnectionService::class.java) {
            SingleIMManager.connectionService
        }
    }
}