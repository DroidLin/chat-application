package com.chat.compose.app.screen.message.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.application.channel.message.SessionType
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.paging.collectAsLazyPagingItems
import com.chat.compose.app.paging.itemContentType
import com.chat.compose.app.paging.itemKey
import com.chat.compose.app.screen.message.vm.SessionDetailViewModel
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.message_detail_send_button_hint
import com.github.droidlin.composeapp.generated.resources.message_detail_text_field_place_holder
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/6/17 00:00
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun SessionDetailScreen(
    sessionId: String,
    sessionType: SessionType,
    backPress: () -> Unit = {}
) {
    val viewModel: SessionDetailViewModel = koinViewModel()
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    val inputTextState = remember { viewModel.inputText }
    val lazyPagingItems = remember(sessionId, sessionType) {
        viewModel.openSession(sessionId, sessionType)
    }.collectAsLazyPagingItems()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveDraft()
            viewModel.closeSession()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "title"
                )
            },
            navigationIcon = {
                IconButton(onClick = backPress) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            reverseLayout = true
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.uuid },
                contentType = lazyPagingItems.itemContentType { it.javaClass.name }
            ) {

            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                value = inputTextState.value,
                onValueChange = { inputTextState.value = it },
                shape = MaterialTheme.shapes.large,
                placeholder = {
                    Text(text = stringResource(Res.string.message_detail_text_field_place_holder))
                }
            )
            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {}
            ) {
                Text(text = stringResource(Res.string.message_detail_send_button_hint))
            }
        }
    }
}