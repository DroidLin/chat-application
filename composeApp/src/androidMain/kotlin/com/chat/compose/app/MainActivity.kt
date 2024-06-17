package com.chat.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MessageListViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lazyListState = rememberLazyListState()
            val lazyPagingItems = this@MainActivity.viewModel.flow.collectAsLazyPagingItems()
            MaterialTheme {
                Column {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f),
                        reverseLayout = true,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = lazyPagingItems.itemKey { it.uuid },
                            contentType = lazyPagingItems.itemContentType { it.sessionType.name }
                        ) { index ->
                            when (val item = lazyPagingItems[index]) {
                                is UiTextMessage -> {
                                    Row(
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .animateItemPlacement()
                                            .clickable {
                                                this@MainActivity.viewModel.deleteMessage(item.uuid, item.sessionType)
                                            }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            modifier = Modifier.weight(1f),
                                            text = item.textContent
                                        )
                                    }
                                }

                                else -> {
                                    Text(
                                        text = "Placeholder",
                                        modifier = Modifier.padding(vertical = 16.dp).fillParentMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                    Row {
                        TextButton(
                            onClick = {
                                this@MainActivity.viewModel.insertMessage(messages)
                            },
                        ) {
                            Text(text = "add")
                        }
                        TextButton(
                            onClick = {

                            },
                        ) {
                            Text(text = "delete")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            strokeCap = StrokeCap.Round,
            strokeWidth = 3.dp,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Loading")
    }
}