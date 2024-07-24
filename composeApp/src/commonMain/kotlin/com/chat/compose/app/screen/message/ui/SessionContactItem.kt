package com.chat.compose.app.screen.message.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.chat.compose.app.metadata.UiRecentContact
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_draft_message_hint
import org.jetbrains.compose.resources.stringResource

@Composable
expect fun SessionContactItem(
    value: UiRecentContact,
    modifier: Modifier = Modifier,
    onPrimaryMouseClick: () -> Unit,
    onSecondaryMouseClick: () -> Unit,
)

@Composable
internal fun UiRecentContact.rememberShowingContent(): AnnotatedString {
    val colorScheme = MaterialTheme.colorScheme
    val draftMessage = this.draftMessage
    val showingContent = this.showingContent

    val draftMessageHint = stringResource(Res.string.string_draft_message_hint)
    return remember(colorScheme, draftMessage, showingContent) {
        val builder = AnnotatedString.Builder()
        if (!draftMessage.isNullOrEmpty()) {
            val hint = AnnotatedString(
                text = draftMessageHint,
                spanStyle = SpanStyle(
                    color = colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            )
            builder.append(hint)
            builder.append(draftMessage)
        } else builder.append(showingContent)
        builder.toAnnotatedString()
    }
}