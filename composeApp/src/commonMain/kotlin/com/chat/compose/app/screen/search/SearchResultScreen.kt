package com.chat.compose.app.screen.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.ui.NameAvatarImage
import com.chat.compose.app.ui.framework.Box
import com.chat.compose.app.ui.framework.Column
import com.chat.compose.app.ui.framework.Row

/**
 * @author liuzhongao
 * @since 2024/7/11 00:19
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    keyword: String,
    backPressed: () -> Unit,
    navigateToUseBasicScreen: (Long) -> Unit,
) {
    val inputText = rememberUpdatedState(keyword)
    val viewModel = koinViewModel<SearchResultViewModel>()

    val uiState = viewModel.uiState.collectAsState()

    val resultState = remember(viewModel, inputText.value) {
        viewModel.searchComplexFlow(query = inputText.value)
    }.collectAsState()

    Column {
        TopAppBar(
            title = {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = CircleShape,
                    onClick = backPressed
                ) {
                    Text(
                        text = inputText.value,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = backPressed) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
        val isLoadingState = remember { derivedStateOf { uiState.value.isLoading } }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val isLoading = isLoadingState.value
            val searchResultResource = resultState.value
            if (isLoading) {
                item(
                    key = "loading"
                ) {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        CircularProgressIndicator()
                    }
                }
            } else if (searchResultResource == null) {
                item(
                    key = "empty"
                ) {
                    Box(modifier = Modifier.fillParentMaxSize()) {

                    }
                }
            } else {
                items(
                    items = searchResultResource.userInfo,
                    key = { it.userInfo?.userId ?: -1 },
                    contentType = { it.javaClass.name }
                ) { profile ->
                    Surface(
                        onClick = {
                            val userId = profile.userInfo?.userId
                            if (userId != null) {
                                navigateToUseBasicScreen(userId)
                            }
                        },
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NameAvatarImage(
                                name = profile.userInfo?.userName ?: "N",
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = profile.userInfo?.userName ?: "N", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}