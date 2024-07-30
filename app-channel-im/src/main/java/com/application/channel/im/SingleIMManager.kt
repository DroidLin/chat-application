package com.application.channel.im

import com.application.channel.message.metadata.RecentContact
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * @author liuzhongao
 * @since 2024/6/22 20:16
 */
object SingleIMManager {

    private val reConnectProcessor = ReConnectProcessor(maxReConnectCount = 5) {
        val initConfig = this.imInitConfig ?: return@ReConnectProcessor
        this.coroutineScope.launch {
            delay(5000)
            this@SingleIMManager.startService(initConfig, true)
        }
    }
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _recentContactFlow = MutableStateFlow<List<RecentContact>>(emptyList())
    val recentContact = _recentContactFlow.asStateFlow()

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
        this.connectionService.messageRepository.fetchRecentContactsFlow(Int.MAX_VALUE)
            .onEach { recentContactList -> this._recentContactFlow.emit(recentContactList) }
            .let { this.flowCoroutineJob?.cancel(); this.flowCoroutineJob = this.coroutineScope.launch { it.collect() } }
    }

    @JvmOverloads
    fun startService(initConfig: IMInitConfig, force: Boolean = false) {
        if (this.imInitConfig == initConfig && !force) {
            return
        }
        this.imInitConfig = initConfig
        this.connectionService.startService(initConfig, force)
    }

    @JvmStatic
    fun onApplicationForeground() {

    }

    fun stopService() {
        this.connectionService.stopService()
    }

    fun release() {
        this.connectionService.release()
        this.flowCoroutineJob?.cancel()
        this.flowCoroutineJob = null
        this.databaseInitConfig = null
        this.imInitConfig = null
    }
}
