package com.chat.compose.app

import android.app.Application
import android.content.Context
import android.os.Debug
import android.os.Looper
import com.chat.compose.app.init.initCore
import java.io.File

/**
 * @author liuzhongao
 * @since 2024/6/9 20:02
 */
class BasicApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initCore(this)
    }
}