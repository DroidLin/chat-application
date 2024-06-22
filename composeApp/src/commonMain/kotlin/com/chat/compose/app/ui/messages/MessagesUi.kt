package com.chat.compose.app.ui.messages

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.chat.compose.app.metadata.UiMessageItem

/**
 * @author liuzhongao
 * @since 2024/6/22 12:20
 */
@Composable
fun MessageUi(uiMessageItem: UiMessageItem, modifier: Modifier = Modifier) {
    val messageItem = rememberUpdatedState(uiMessageItem)

    val sessionContact by remember { derivedStateOf { messageItem.value.uiSessionContact } }
    val message by remember { derivedStateOf { messageItem.value.uiMessage } }

    val isSenderMessage by remember { derivedStateOf { message.isSenderMessage } }
    val isReceiverMessage by remember { derivedStateOf { message.isReceiverMessage } }

    if (isSenderMessage && isReceiverMessage) {
        // chat with self condition
        SenderMessageItem(messageItem.value, modifier)
    } else if (isSenderMessage && !isReceiverMessage) {
        SenderMessageItem(messageItem.value, modifier)
    } else if (isReceiverMessage && !isSenderMessage) {
        ReceiverMessageItem(messageItem.value, modifier)
    }
}