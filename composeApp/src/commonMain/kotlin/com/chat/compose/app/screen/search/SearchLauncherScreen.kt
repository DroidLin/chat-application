package com.chat.compose.app.screen.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.ui.framework.Box
import com.chat.compose.app.ui.framework.Column
import com.chat.compose.app.ui.ime.FocusClearMan
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_button_search
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/7/1 00:00
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLauncherScreen(
    backPressed: () -> Unit = {}
) {
    val viewModel = koinViewModel<SearchLauncherViewModel>()
    val uiState = viewModel.uiState.collectAsState()
    FocusClearMan()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                val inputText by remember { derivedStateOf { uiState.value.searchInputKeyword } }
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = CircleShape,
                ) {
                    val contentColor = LocalContentColor.current
                    val selectionColor by remember { derivedStateOf { contentColor.copy(alpha = 0.4f) } }
                    CompositionLocalProvider(LocalTextSelectionColors provides TextSelectionColors(contentColor, selectionColor)) {
                        val focusRequester = remember { FocusRequester() }
                        LaunchedEffect(focusRequester) {
                            focusRequester.requestFocus()
                        }
                        BasicTextField(
                            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                            value = inputText,
                            onValueChange = viewModel::onInputChange,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
                            decorationBox = { basicField ->
                                Box(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    basicField()
                                }
                            },
                            singleLine = true,
                            cursorBrush = SolidColor(contentColor),
                            keyboardActions = KeyboardActions(
                                onDone = { viewModel.onSearch() }
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = backPressed) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                TextButton(onClick = viewModel::onSearch) {
                    Text(stringResource(Res.string.string_button_search))
                }
            }
        )
    }
}