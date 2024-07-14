package com.chat.compose.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * @author liuzhongao
 * @since 2024/6/19 00:27
 */

private val DefaultAvatarSize = 48.dp

@Composable
fun NameAvatarImage(
    name: String,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
) {
    InnerNameAvatarImage(
        name = name,
        modifier = modifier,
        style = style
    ) { content ->
        Surface(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = CircleShape,
            content = content
        )
    }
}


@Composable
fun NameAvatarImage(
    name: String,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    onClick: () -> Unit,
) {
    InnerNameAvatarImage(
        name = name,
        modifier = modifier,
        style = style
    ) { content ->
        Surface(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = CircleShape,
            content = content,
            onClick = onClick
        )
    }
}

@Composable
private fun InnerNameAvatarImage(
    name: String,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    container: @Composable (@Composable () -> Unit) -> Unit
) {
    val nameState = rememberUpdatedState(name)
    val singleWord = remember { derivedStateOf { nameState.value.getOrNull(0)?.uppercase() ?: "" } }

    container {
        Box(
            modifier = Modifier.then(modifier).size(DefaultAvatarSize),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = singleWord.value,
                style = style ?: MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}