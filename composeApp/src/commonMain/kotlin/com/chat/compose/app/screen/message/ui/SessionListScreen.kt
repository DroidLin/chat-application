package com.chat.compose.app.screen.message.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.metadata.UiSessionContact
import com.chat.compose.app.screen.message.vm.SessionListViewModel
import com.chat.compose.app.ui.appSafeAreaPadding

/**
 * @author liuzhongao
 * @since 2024/6/16 21:56
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SessionListScreen(
    backPress: () -> Unit = {},
    sessionItemClick: (UiSessionContact) -> Unit = {},
    navigateToSearch: () -> Unit = {}
) {
    val viewModel: SessionListViewModel = koinViewModel()
    val sessionListState = remember(viewModel) { viewModel.sessionList }.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateSessionContactInfo()
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .appSafeAreaPadding()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
    ) {
        TopAppBar(
            title = { Text(text = "Session") },
            actions = {
                IconButton(onClick = navigateToSearch) {
                    Icon(Icons.Filled.Search, contentDescription = "search")
                }
            }
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            val sessionList = sessionListState.value
            items(
                items = sessionList,
                key = { it.sessionId },
                contentType = { it.javaClass.name }
            ) { uiSessionContact ->
                SessionContactItem(
                    value = uiSessionContact,
                    modifier = Modifier.fillParentMaxWidth().animateItemPlacement(),
                    onPrimaryMouseClick = { sessionItemClick(uiSessionContact) },
                    onSecondaryMouseClick = {}
                )
            }
        }
    }
}