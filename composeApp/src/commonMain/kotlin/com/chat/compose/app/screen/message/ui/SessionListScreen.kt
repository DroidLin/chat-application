package com.chat.compose.app.screen.message.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.metadata.UiRecentContact
import com.chat.compose.app.screen.message.vm.SessionListViewModel
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.homeNavigationComposable
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_recent_contact_title
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/6/16 21:56
 */

fun NavGraphBuilder.sessionListScreen(
    sessionItemClick: (UiRecentContact) -> Unit = {},
    navigateToSearch: () -> Unit = {}
) {
    homeNavigationComposable(
        route = NavRoute.ChatSessionList.route,
    ) {
        SessionListScreen(
            sessionItemClick = sessionItemClick,
            navigateToSearch = navigateToSearch,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SessionListScreen(
    modifier: Modifier = Modifier,
    sessionItemClick: (UiRecentContact) -> Unit = {},
    navigateToSearch: () -> Unit = {}
) {
    val viewModel: SessionListViewModel = koinViewModel()
    val sessionListState = remember(viewModel) { viewModel.recentContactList }.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
    ) {
        TopAppBar(
            title = { Text(text = stringResource(Res.string.string_recent_contact_title)) },
            actions = {
                IconButton(onClick = navigateToSearch) {
                    Icon(Icons.Filled.Search, contentDescription = "search")
                }
            }
        )
        com.chat.compose.app.ui.framework.Box(
            modifier = Modifier.weight(1f)
        ) {
            val lazyListState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier,
                state = lazyListState,
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
}