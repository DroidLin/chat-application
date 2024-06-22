package com.chat.compose.app.screen.message.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * @author liuzhongao
 * @since 2024/6/21 21:11
 */
@Composable
expect fun ChatInputArea(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
)
