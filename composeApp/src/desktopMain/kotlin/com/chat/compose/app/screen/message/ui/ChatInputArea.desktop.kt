package com.chat.compose.app.screen.message.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.message_detail_send_button_hint
import com.github.droidlin.composeapp.generated.resources.message_detail_text_field_place_holder
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/6/21 21:11
 */
@Composable
actual fun ChatInputArea(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier
) {
    val inputText = rememberUpdatedState(text)
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .onPreviewKeyEvent {
                    if (it.isShiftPressed && it.key == Key.Enter) {
                        onTextChange("${inputText.value}\n")
                        true
                    } else if (!it.isShiftPressed && !it.isAltPressed && !it.isCtrlPressed) {
                        when (val key = it.key) {
                            Key.Enter -> {
                                onSendClick()
                                true
                            }

                            else -> false
                        }
                    } else false
                },
            value = text,
            onValueChange = onTextChange,
            shape = MaterialTheme.shapes.large,
            placeholder = {
                Text(text = stringResource(Res.string.message_detail_text_field_place_holder))
            }
        )
        val sendButtonEnable by remember { derivedStateOf { inputText.value.isNotEmpty() } }
        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onSendClick,
            enabled = sendButtonEnable,
        ) {
            Text(text = stringResource(Res.string.message_detail_send_button_hint))
        }
    }
}