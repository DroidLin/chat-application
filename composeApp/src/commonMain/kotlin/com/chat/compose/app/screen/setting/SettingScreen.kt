package com.chat.compose.app.screen.setting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chat.compose.app.ui.framework.Column

/**
 * @author liuzhongao
 * @since 2024/6/16 21:44
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    backPressed: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Setting") },
            navigationIcon = {
                IconButton(onClick = { backPressed() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }
}