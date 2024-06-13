package com.chat.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

/**
 * @author liuzhongao
 * @since 2024/6/9 20:06
 */
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MessageListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        this.viewModel.insertMessage(messages)

        setContent {
            MaterialTheme {
                val lazyListState = rememberLazyListState()
                val lazyPagingItems = this.viewModel.flow.collectAsLazyPagingItems()
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    reverseLayout = true,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.uuid },
                        contentType = lazyPagingItems.itemContentType { it.javaClass }
                    ) { index ->
                        val item = requireNotNull(lazyPagingItems[index])
                        if (item is UiTextMessage) {
                            Row(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .clickable {
                                        this@MainActivity.viewModel.deleteMessage(item.uuid, item.sessionType)
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$index"
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.textContent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}