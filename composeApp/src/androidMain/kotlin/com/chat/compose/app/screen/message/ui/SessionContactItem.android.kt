package com.chat.compose.app.screen.message.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chat.compose.app.metadata.UiSessionContact
import com.chat.compose.app.ui.NameAvatarImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun SessionContactItem(
    value: UiSessionContact,
    modifier: Modifier,
    onPrimaryMouseClick: () -> Unit,
    onSecondaryMouseClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .graphicsLayer(shape = MaterialTheme.shapes.large)
            .combinedClickable(
                onClick = onPrimaryMouseClick,
                onLongClick = onSecondaryMouseClick,
            ),
        color = Color.Transparent
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
                    Text(
                        modifier = Modifier.weight(1f),
                        text = value.displayContent,
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