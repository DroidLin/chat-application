package com.chat.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems

/**
 * @author liuzhongao
 * @since 2024/6/9 20:06
 */
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MessageListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModel.insertMessage(messages)

        setContent {
            MaterialTheme {
                val lazyPagingItems = this.viewModel.flow.collectAsLazyPagingItems()
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    for (index in 0 until lazyPagingItems.itemCount) {
                        val item = lazyPagingItems[index] ?: continue
                        item(
                            key = item.uuid,
                            contentType = item.javaClass,
                        ) {
                            if (item is UiTextMessage) {
                                Text(
                                    modifier = Modifier
                                        .fillParentMaxWidth()
                                        .padding(16.dp)
                                        .clickable {
                                            this@MainActivity.viewModel.deleteMessage(item.uuid, item.sessionType)
                                        },
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