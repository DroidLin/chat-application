package com.chat.compose.app.screen.message.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chat.compose.app.ui.ime.FocusClearMan
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
    FocusClearMan()
    val inputText = rememberUpdatedState(text)
    Row(
        modifier = modifier.padding(all = 8.dp)
            .imePadding()
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .heightIn(max = 180.dp),
            value = inputText.value,
            onValueChange = onTextChange,
            shape = MaterialTheme.shapes.large,
            placeholder = {
                Text(text = stringResource(Res.string.message_detail_text_field_place_holder))
            }
        )
        val sendButtonEnable by remember { derivedStateOf { inputText.value.isNotEmpty() } }
        Button(
            modifier = Modifier,
            onClick = onSendClick,
            enabled = sendButtonEnable
        ) {
            Text(text = stringResource(Res.string.message_detail_send_button_hint))
        }
    }
}