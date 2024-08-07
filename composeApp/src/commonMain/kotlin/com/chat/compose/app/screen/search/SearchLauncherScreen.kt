package com.chat.compose.app.screen.search

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.framework.Box
import com.chat.compose.app.ui.framework.Column
import com.chat.compose.app.ui.ime.FocusClearMan
import com.chat.compose.app.ui.navigationComposable
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_button_clear_search_history
import com.github.droidlin.composeapp.generated.resources.string_button_search
import com.github.droidlin.composeapp.generated.resources.string_button_search_history
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/7/1 00:00
 */

fun NavGraphBuilder.searchLauncherScreen(
    backPressed: () -> Unit,
    navigateToSearchResult: (String) -> Unit,
) {
    navigationComposable(
        route = NavRoute.SearchLauncher.route
    ) {
        SearchLauncherScreen(
            backPressed = backPressed,
            navigateToSearchResult = navigateToSearchResult,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchLauncherScreen(
    backPressed: () -> Unit = {},
    navigateToSearchResult: (String) -> Unit = {},
) {
    val viewModel = koinViewModel<SearchLauncherViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val searchDefault = {
        viewModel.onSearch()
        navigateToSearchResult(uiState.value.searchInputKeyword)
    }

    FocusClearMan()
    Column(
        modifier = Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
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
                    val selectionColors = remember(contentColor, selectionColor) {
                        TextSelectionColors(contentColor, selectionColor)
                    }
                    CompositionLocalProvider(
                        LocalTextSelectionColors provides selectionColors
                    ) {
                        val focusRequester = remember { FocusRequester() }
                        LaunchedEffect(focusRequester) {
                            focusRequester.requestFocus()
                        }
                        BasicTextField(
                            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                            value = inputText,
                            onValueChange = viewModel::onInputChange,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
                            decorationBox = { basicTextField ->
                                Row(
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        basicTextField()
                                    }
                                    val clearBtnVisible by remember { derivedStateOf { uiState.value.searchInputKeyword.isNotEmpty() } }
                                    AnimatedVisibility(
                                        visible = clearBtnVisible,
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .clickable { viewModel.onInputChange("") }
                                                .padding(all = 2.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            cursorBrush = SolidColor(contentColor),
                            keyboardActions = KeyboardActions(
                                onDone = { searchDefault() }
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
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
                val buttonEnable by remember { derivedStateOf { uiState.value.searchInputKeyword.isNotEmpty() } }
                TextButton(
                    enabled = buttonEnable,
                    onClick = searchDefault
                ) {
                    Text(stringResource(Res.string.string_button_search))
                }
            }
        )
        val historyConfig by viewModel.historyConfig.collectAsStateWithLifecycle()
        if (historyConfig.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(Res.string.string_button_search_history))
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = viewModel::clearAllHistory,
                        modifier = Modifier
                    ) {
                        Text(stringResource(Res.string.string_button_clear_search_history))
                    }
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    historyConfig.forEach { config ->
                        AssistChip(
                            modifier = Modifier,
                            onClick = {
                                viewModel.onInputChange(config.value)
                                viewModel.onSearch(config.value)
                                navigateToSearchResult(config.value)
                            },
                            label = {
                                Text(text = config.value)
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable(
                                            onClick = { viewModel.deleteHistoryConfig(config) },
                                            interactionSource = null,
                                            indication = null,
                                        ),
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}