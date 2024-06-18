package com.chat.compose.app.screen.message.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.screen.message.vm.SessionListViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle

/**
 * @author liuzhongao
 * @since 2024/6/16 21:56
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionListScreen(
    backPress: () -> Unit = {}
) {
    val viewModel: SessionListViewModel = koinViewModel()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Session") },
        )
        val sessionList by viewModel.sessionList.collectAsStateWithLifecycle()
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(
                items = sessionList,
                key = { it.sessionId },
                contentType = { it.javaClass.name }
            ) { uiSessionContact ->
                SessionContactItem(
                    value = uiSessionContact,
                    modifier = Modifier.fillParentMaxWidth(),
                    onClick = {}
                )
            }
        }
    }
}