package com.chat.compose.app.screen.message.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chat.compose.app.metadata.UiSessionContact

@Composable
actual fun SessionContactItem(
    value: UiSessionContact,
    modifier: Modifier,
    onPrimaryMouseClick: () -> Unit,
    onSecondaryMouseClick: () -> Unit
) {
}