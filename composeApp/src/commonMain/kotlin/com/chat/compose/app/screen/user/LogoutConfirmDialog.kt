package com.chat.compose.app.screen.user

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.github.droidlin.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/7/27 09:49
 */
@Composable
fun LogoutConfirmDialog(
    enable: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!enable) return
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(Res.string.string_logout_label))
        },
        text = {
            Text(text = stringResource(Res.string.string_all_personal_information_will_delete))
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(text = stringResource(Res.string.string_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(Res.string.string_cancel))
            }
        }
    )
}