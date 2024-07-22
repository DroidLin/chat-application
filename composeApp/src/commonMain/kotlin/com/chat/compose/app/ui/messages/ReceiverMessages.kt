package com.chat.compose.app.ui.messages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chat.compose.app.metadata.TextUiMessage
import com.chat.compose.app.metadata.UiMessageItem
import com.chat.compose.app.ui.NameAvatarImage

/**
 * @author liuzhongao
 * @since 2024/6/22 11:45
 */

@Composable
fun ReceiverMessageItem(uiMessageItem: UiMessageItem, modifier: Modifier = Modifier, onAvatarClick: () -> Unit = {}) {
    val messageItem = rememberUpdatedState(uiMessageItem)

    val sessionContact by remember { derivedStateOf { messageItem.value.uiSessionContact } }
    val message by remember { derivedStateOf { messageItem.value.uiMessage } }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        NameAvatarImage(
            name = sessionContact?.sessionContactName ?: "",
            modifier = Modifier,
            onClick = onAvatarClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            when (val uiMessage = message) {
                is TextUiMessage -> TextUiMessageItem(uiMessage)
            }
        }
    }
}

@Composable
private fun TextUiMessageItem(uiMessage: TextUiMessage, modifier: Modifier = Modifier) {
    val message by rememberUpdatedState(uiMessage)
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium.copy(topStart = CornerSize(0.dp)),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Box(
            modifier = Modifier.heightIn(min = 48.dp),
            contentAlignment = Alignment.Center
        ) {
//            SelectionContainer {
                val textContent by remember { derivedStateOf { message.textContent } }
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    text = textContent,
                    style = MaterialTheme.typography.bodyMedium,
                )
//            }
        }
    }
}