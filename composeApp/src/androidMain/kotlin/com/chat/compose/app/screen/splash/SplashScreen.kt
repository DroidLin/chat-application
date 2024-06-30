package com.chat.compose.app.screen.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chat.compose.app.initBiz
import com.chat.compose.app.ui.framework.Box
import kotlinx.coroutines.delay

/**
 * @author liuzhongao
 * @since 2024/6/30 20:41
 */
@Composable
fun SplashScreen(
    onInitialFinished: () -> Unit
) {
    LaunchedEffect(onInitialFinished) {
        initBizComponent()
        delay(500L)
        onInitialFinished()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(strokeWidth = 4.dp, modifier = Modifier.size(56.dp))
    }
}

private var initialized: Boolean = false

private suspend fun initBizComponent() {
    if (initialized) return

    initBiz()

    initialized = true
}