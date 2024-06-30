package com.application.channel.im

import com.application.channel.message.metadata.SessionContact
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

/**
 * @author liuzhongao
 * @since 2024/6/22 20:16
 */
object SingleIMManager {

    private val reConnectProcessor = ReConnectProcessor(maxReConnectCount = 5) {
        val initConfig = this.imInitConfig ?: return@ReConnectProcessor
        this.startService(initConfig, true)
    }
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _sessionContactFlow = MutableSharedFlow<List<SessionContact>>()
    val sessionContact = _sessionContactFlow.asSharedFlow()

    val connectionService = MsgClient.getService(MsgConnectionService::class.java)
    val msgService = MsgClient.getService(MsgService::class.java)

    private var databaseInitConfig: IMDatabaseInitConfig? = null
    private var imInitConfig: IMInitConfig? = null
    private var flowCoroutineJob: Job? = null

    init {
        this.connectionService.addEventObserver(this.reConnectProcessor)
    }

    fun initDatabase(initConfig: IMDatabaseInitConfig) {
        if (this.databaseInitConfig == initConfig) {
            return
        }
        this.databaseInitConfig = initConfig
        this.connectionService.initDatabase(initConfig)
        this.connectionService.messageRepository.fetchObservableSessionContactList(Int.MAX_VALUE)
            .onEach { sessionContactList -> this._sessionContactFlow.emit(sessionContactList) }
            .let { this.flowCoroutineJob = this.coroutineScope.launch { it.collect() } }
    }

    @JvmOverloads
    fun startService(initConfig: IMInitConfig, force: Boolean = false) {
        if (this.imInitConfig == initConfig && !force) {
            return
        }
        this.imInitConfig = initConfig
        this.connectionService.startService(initConfig)
    }

    fun stopService() {
        this.connectionService.stopService()
    }

    fun release() {
        this.connectionService.release()
    }
}
