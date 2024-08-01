package com.chat.compose.app.ui.messages

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.chat.compose.app.metadata.UiMessageItem

/**
 * @author liuzhongao
 * @since 2024/6/22 12:20
 */
@Composable
fun MessageUi(
    uiMessageItem: UiMessageItem,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {}
) {
    val isSenderMessage = uiMessageItem.uiMessage.isSenderMessage
    val isReceiverMessage = uiMessageItem.uiMessage.isReceiverMessage

    if (isSenderMessage && isReceiverMessage) {
        // chat with self condition
        SenderMessageItem(uiMessageItem, modifier, onAvatarClick)
    } else if (isSenderMessage && !isReceiverMessage) {
        SenderMessageItem(uiMessageItem, modifier, onAvatarClick)
    } else if (isReceiverMessage && !isSenderMessage) {
        ReceiverMessageItem(uiMessageItem, modifier, onAvatarClick)
    }
}