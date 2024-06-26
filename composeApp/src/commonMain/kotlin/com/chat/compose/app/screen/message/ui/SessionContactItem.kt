package com.chat.compose.app.screen.message.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chat.compose.app.metadata.UiSessionContact

@Composable
expect fun SessionContactItem(
    value: UiSessionContact,
    modifier: Modifier = Modifier,
    onPrimaryMouseClick: () -> Unit,
    onSecondaryMouseClick: () -> Unit,
)