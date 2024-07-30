package com.application.channel.message

import com.application.channel.core.client.TcpConnectionObserver
import com.application.channel.core.model.ChannelContext
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * @author liuzhongao
 * @since 2024/6/23 11:36
 */
interface ChatServiceEventObserver : TcpConnectionObserver {

    fun onDatabaseInitialized()

    fun onLogin()

    fun onLoginFailure(event: FailureType, message: String?)

    fun onLogout(type: LogoutReason)
}

class MutableChatServiceEventObserver : ChatServiceEventObserver {

    private val reentrantLock = ReentrantLock()
    private val observerList = mutableListOf<ChatServiceEventObserver>()

    fun addObserver(observer: ChatServiceEventObserver) {
        this.reentrantLock.withLock {
            if (!this.observerList.contains(observer)) {
                this.observerList.add(observer)
            }
        }
    }

    fun removeObserver(observer: ChatServiceEventObserver) {
        this.reentrantLock.withLock {
            val iterator = this.observerList.iterator()
            while (iterator.hasNext()) {
                val tmpObserver = iterator.next()
                if (tmpObserver == observer) {
                    this.observerList.remove(observer)
                }
            }
        }
    }

    override fun onConnectionSuccess(channelCtx: ChannelContext) {
        this.notifyObservers {
            it.onConnectionSuccess(channelCtx)
        }
    }

    override fun onDatabaseInitialized() {
        this.notifyObservers {
            it.onDatabaseInitialized()
        }
    }

    override fun onLogin() {
        this.notifyObservers {
            it.onLogin()
        }
    }

    override fun onLoginFailure(event: FailureType, message: String?) {
        this.notifyObservers {
            it.onLoginFailure(event, message)
        }
    }

    override fun onLogout(type: LogoutReason) {
        this.notifyObservers {
            it.onLogout(type)
        }
    }

    override fun onStartConnect() {
        this.notifyObservers {
            it.onStartConnect()
        }
    }

    override fun onConnectionFailure(channelCtx: ChannelContext, throwable: Throwable?) {
        this.notifyObservers {
            it.onConnectionFailure(channelCtx, throwable)
        }
    }

    override fun onConnectionLost(channelCtx: ChannelContext, throwable: Throwable?) {
        this.notifyObservers {
            it.onConnectionLost(channelCtx, throwable)
        }
    }

    private fun notifyObservers(observer: (ChatServiceEventObserver) -> Unit) {
        this.reentrantLock.withLock {
            val iterator = this.observerList.iterator()
            while (iterator.hasNext()) {
                val tmpObserver = iterator.next()
                tmpObserver.apply(observer)
            }
        }
    }

    fun clear() {
        this.reentrantLock.withLock {
            this.observerList.clear()
        }
    }
}