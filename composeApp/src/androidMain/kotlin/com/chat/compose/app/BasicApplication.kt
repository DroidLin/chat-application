package com.chat.compose.app

import android.app.Application
import android.content.Context
import android.os.Debug
import android.os.Looper
import com.application.channel.im.MsgConnectionService
import com.chat.compose.app.di.messageModule
import com.chat.compose.app.di.useCaseModule
import com.chat.compose.app.di.viewModelModule
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import java.io.File

/**
 * @author liuzhongao
 * @since 2024/6/9 20:02
 */
class BasicApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        Debug.startMethodTracingSampling(File(cacheDir, "profile.trace").absolutePath, 1 shl 20, 100)
//        Looper.getMainLooper().queue.addIdleHandler {
//            Debug.stopMethodTracing()
//            false
//        }
        basicApplication = this
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                viewModelModule,
                messageModule,
                useCaseModule,
            )
        }

        val service: MsgConnectionService = GlobalContext.get().get()
        service.initService(GlobalContext.get().get())

    }

    companion object {
        private var basicApplication: BasicApplication? = null
        val applicationContext: Context get() = requireNotNull(this.basicApplication)
    }
}