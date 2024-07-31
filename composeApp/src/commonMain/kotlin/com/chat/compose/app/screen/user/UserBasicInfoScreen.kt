package com.chat.compose.app.screen.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.application.channel.message.SessionType
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.route.LocalRouteAction
import com.chat.compose.app.ui.NameAvatarImage
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.navigationComposable
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_label_error
import com.github.droidlin.composeapp.generated.resources.string_send_message_to_user
import com.mplayer.common.ui.OverScrollableLazyColumn
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/7/12 00:23
 */

fun NavGraphBuilder.userBasicInfoScreen(
    backPress: () -> Unit,
    navigateToChat: (String, SessionType) -> Unit
) {
    navigationComposable(
        route = NavRoute.UserBasicInfo.route,
        arguments = listOf(
            navArgument(name = "userId") {
                type = NavType.LongType
                nullable = false
            }
        )
    ) { backStackEntry ->
        val userId = requireNotNull(backStackEntry.arguments?.getLong("userId"))
        UserBasicInfoScreen(
            userId = userId,
            backPress = backPress,
            navigateToChat = navigateToChat
        )
    }
}

@Composable
fun UserBasicInfoScreen(
    userId: Long,
    backPress: () -> Unit,
    navigateToChat: (String, SessionType) -> Unit
) {
    val viewModel = koinViewModel<UserBasicInfoViewModel>()
    LaunchedEffect(userId) {
        viewModel.fetchUserInfo(userId)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is UserBasicInfoState.Loading -> UserBasicInfoLoadingContent()
        is UserBasicInfoState.Error -> UserBasicInfoErrorContent(state, backPress)
        is UserBasicInfoState.UserBasicInfoUiState -> UserBasicInfoContent(state, backPress, navigateToChat)
    }
}

@Composable
private fun UserBasicInfoLoadingContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserBasicInfoErrorContent(
    state: UserBasicInfoState.Error,
    backPress: () -> Unit
) {
    Column {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = backPress) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
        Box(
            modifier = Modifier.fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(Modifier.height(30.dp))
                Text(
                    text = stringResource(Res.string.string_label_error),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(text = state.message, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserBasicInfoContent(
    state: UserBasicInfoState.UserBasicInfoUiState,
    backPress: () -> Unit,
    navigateToChat: (String, SessionType) -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(text = state.userName)
            },
            navigationIcon = {
                IconButton(
                    onClick = backPress
                ) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            }
        )

        val sessionInfoAvailable = remember {
            derivedStateOf {
                state.sessionId.isNotEmpty() && state.sessionType != SessionType.Unknown
            }
        }
        OverScrollableLazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item(
                key = "user_avatar"
            ) {
                NameAvatarImage(name = state.userName, modifier = Modifier.size(56.dp))
            }
            item(
                key = "user_name"
            ) {
                Text(text = state.userName, style = MaterialTheme.typography.titleMedium)
            }
            if (sessionInfoAvailable.value) {
                item(
                    key = "operations"
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    ) {
                        FilledTonalButton(
                            onClick = {
                                navigateToChat(state.sessionId, state.sessionType)
                            }
                        ) {
                            Text(text = stringResource(Res.string.string_send_message_to_user))
                        }
                    }
                }
            }
        }
    }
}