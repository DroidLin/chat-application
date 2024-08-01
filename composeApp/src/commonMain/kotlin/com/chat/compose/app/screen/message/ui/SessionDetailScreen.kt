package com.chat.compose.app.screen.message.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.application.channel.message.SessionType
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.paging.collectAsLazyPagingItems
import com.chat.compose.app.paging.itemContentType
import com.chat.compose.app.paging.itemKey
import com.chat.compose.app.screen.message.vm.ChatDetailUiState
import com.chat.compose.app.screen.message.vm.SessionDetailViewModel
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.fastScrollToPosition
import com.chat.compose.app.ui.ime.FocusClearMan
import com.chat.compose.app.ui.messages.MessageUi
import com.chat.compose.app.ui.navigationComposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/17 00:00
 */

fun NavGraphBuilder.chatDetailScreen(
    backPress: () -> Unit,
    navigateToUserBasicInfo: (Long) -> Unit,
) {
    navigationComposable(
        route = NavRoute.ChatMessageDetail.route,
        arguments = listOf(
            navArgument(name = "sessionId") {
                type = NavType.StringType
            },
            navArgument(name = "sessionType") {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val sessionId: String =
            requireNotNull(backStackEntry.arguments?.getString("sessionId"))
        val sessionType: SessionType = SessionType.fromValue(
            requireNotNull(backStackEntry.arguments?.getString("sessionType")?.toIntOrNull())
        )
        SessionDetailScreen(
            sessionId = sessionId,
            sessionType = sessionType,
            backPress = backPress,
            navigateToUserBasicInfo = navigateToUserBasicInfo
        )
    }
}

@Composable
fun SessionDetailScreen(
    sessionId: String,
    sessionType: SessionType,
    backPress: () -> Unit,
    navigateToUserBasicInfo: (Long) -> Unit,
) {
    val viewModel: SessionDetailViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(viewModel, sessionId, sessionType, coroutineScope) {
        coroutineScope.launch {
            viewModel.openSession(sessionId, sessionType)
        }
        onDispose {
            viewModel.saveDraft()
            viewModel.closeSession()
        }
    }

    SessionDetailContent(
        uiState = uiState.value,
        inputText = viewModel.inputText,
        backPress = backPress,
        navigateToUserBasicInfo = navigateToUserBasicInfo,
        onSend = viewModel::onSendTextMessage,
        onInputValueChange = viewModel::updateInputText
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionDetailContent(
    uiState: ChatDetailUiState,
    inputText: String,
    backPress: () -> Unit,
    navigateToUserBasicInfo: (Long) -> Unit,
    onSend: () -> Unit,
    onInputValueChange: (String) -> Unit,
) {
    val lazyPagingItems = uiState.flow.collectAsLazyPagingItems(Dispatchers.Default)
    val coroutineScope = rememberCoroutineScope()
    FocusClearMan()
    Column(
        modifier = Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
    ) {
        TopAppBar(
            title = {
                Text(
                    modifier = Modifier,
                    text = uiState.showingTitle
                )
            },
            navigationIcon = {
                IconButton(onClick = backPress) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
        Box(
            modifier = Modifier.weight(1f)
                .graphicsLayer { clip = true }
        ) {
            val lazyListState = rememberLazyListState()
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
            ) {
                item(key = "header_place_holder") { Spacer(modifier = Modifier.fillMaxWidth().height(1.dp)) }
                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.uiMessage.uuid },
                    contentType = lazyPagingItems.itemContentType { it.javaClass.name }
                ) { index ->
                    val messageItem = lazyPagingItems[index] ?: return@items
                    MessageUi(
                        modifier = Modifier
                            .fillParentMaxWidth(),
                        uiMessageItem = messageItem,
                        onAvatarClick = {
                            val userId = messageItem.uiSessionContact?.userId
                            if (userId != null) navigateToUserBasicInfo(userId)
                        }
                    )
                }
            }
            val scrollToBottomVisible = remember {
                derivedStateOf {
                    lazyListState.firstVisibleItemIndex > 0
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                visible = scrollToBottomVisible.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FilledIconButton(
                    onClick = {
                        coroutineScope.launch {
                            lazyListState.fastScrollToPosition(0)
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }
        }
        ChatInputArea(
            text = inputText,
            onTextChange = onInputValueChange,
            onSendClick = onSend,
            modifier = Modifier,
        )
    }
}