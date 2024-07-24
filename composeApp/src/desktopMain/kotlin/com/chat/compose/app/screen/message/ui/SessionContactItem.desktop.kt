package com.chat.compose.app.screen.message.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chat.compose.app.metadata.UiRecentContact
import com.chat.compose.app.ui.NameAvatarImage

/**
 * @author liuzhongao
 * @since 2024/6/18 22:10
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun SessionContactItem(
    value: UiRecentContact,
    modifier: Modifier,
    onPrimaryMouseClick: () -> Unit,
    onSecondaryMouseClick: () -> Unit,
) {
    val uiSessionContact by rememberUpdatedState(value)
    Surface(
        modifier = modifier
            .onClick(matcher = PointerMatcher.mouse(PointerButton.Secondary), onClick = onSecondaryMouseClick),
        shape = MaterialTheme.shapes.large,
        onClick = onPrimaryMouseClick
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NameAvatarImage(
                name = value.sessionContactName
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = value.sessionContactName,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = value.time,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    val text= uiSessionContact.rememberShowingContent()
                    Text(
                        modifier = Modifier.weight(1f),
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (value.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge {
                            Text(
                                text = value.unreadCount.toString(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}