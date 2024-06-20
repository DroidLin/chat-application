package com.chat.compose.app

import android.app.Application
import android.content.Context
import com.chat.compose.app.di.messageModule
import com.chat.compose.app.di.useCaseModule
import com.chat.compose.app.di.viewModelModule
import org.koin.core.context.startKoin

/**
 * @author liuzhongao
 * @since 2024/6/9 20:02
 */
class BasicApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
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
    }

    companion object {
        private var basicApplication: BasicApplication? = null
        val applicationContext: Context get() = requireNotNull(this.basicApplication)
    }
}