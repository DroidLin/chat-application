package com.chat.compose.app.screen.setting

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

/**
 * @author liuzhongao
 * @since 2024/6/16 21:44
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    backPressed: () -> Unit = {},
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