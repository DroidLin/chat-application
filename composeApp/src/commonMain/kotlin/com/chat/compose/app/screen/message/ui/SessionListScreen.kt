package com.chat.compose.app.screen.message.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chat.compose.app.di.daggerViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.viewmodel.viewModel

/**
 * @author liuzhongao
 * @since 2024/6/16 21:56
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionListScreen(
    backPress: () -> Unit = {}
) {
    val viewModel = daggerViewModel { it.sessionListViewModel }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Session") },
        )

        val sessionList = viewModel.sessionList.collectAsStateWithLifecycle()
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
        }
    }
}