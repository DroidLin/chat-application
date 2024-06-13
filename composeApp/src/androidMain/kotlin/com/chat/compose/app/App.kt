package com.chat.compose.app

import android.app.Application
import android.content.Context

/**
 * @author liuzhongao
 * @since 2024/6/9 20:02
 */
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        app = this
    }

    companion object {
        private var app: App? = null
        val applicationContext: Context get() = requireNotNull(this.app)
    }
}