package com.application.channel.im

/**
 * @author liuzhongao
 * @since 2024/6/22 20:16
 */
object SingleIMManager {

    private val reConnectProcessor = ReConnectProcessor(maxReConnectCount = 5) {
        val initConfig = this.imInitConfig ?: return@ReConnectProcessor
        this.startService(initConfig, true)
    }
    val connectionState = this.reConnectProcessor.state

    val connectionService = MsgClient.getService(MsgConnectionService::class.java)
    val msgService = MsgClient.getService(MsgService::class.java)

    private var databaseInitConfig: IMDatabaseInitConfig? = null
    private var imInitConfig: IMInitConfig? = null

    init {
        this.connectionService.addEventObserver(this.reConnectProcessor)
    }

    fun initDatabase(initConfig: IMDatabaseInitConfig) {
        if (this.databaseInitConfig == initConfig) {
            return
        }
        this.databaseInitConfig = initConfig
        this.connectionService.initDatabase(initConfig)
    }

    @JvmOverloads
    fun startService(initConfig: IMInitConfig, force: Boolean = false) {
        if (this.imInitConfig == initConfig) {
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

data class SingleIMState(
    val state: State = State.NotInitialized
)
