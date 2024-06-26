package com.chat.compose.app

import android.app.Application
import android.content.Context
import android.os.Debug
import android.os.Looper
import com.android.dependencies.common.android.installContext
import com.application.channel.core.util.koinInject
import com.application.channel.im.IMDatabaseInitConfig
import com.application.channel.im.SingleIMManager
import java.io.File

/**
 * @author liuzhongao
 * @since 2024/6/9 20:02
 */
class BasicApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Debug.startMethodTracingSampling(File(cacheDir, "profile.trace").absolutePath, 1 shl 20, 100)
        Looper.getMainLooper().queue.addIdleHandler {
            Debug.stopMethodTracing()
            false
        }
        initCore(this)
    }
}