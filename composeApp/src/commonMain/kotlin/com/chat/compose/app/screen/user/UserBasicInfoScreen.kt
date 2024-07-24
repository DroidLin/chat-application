package com.chat.compose.app.screen.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.application.channel.message.SessionType
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.router.LocalRouteAction
import com.chat.compose.app.ui.NameAvatarImage
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.navigationComposable
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_send_message_to_user
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
        val routeAction = LocalRouteAction.current
        val userId = requireNotNull(backStackEntry.arguments?.getLong("userId"))
        UserBasicInfoScreen(
            userId = userId,
            backPress = backPress,
            navigateToChat = navigateToChat
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    val uiState = viewModel.uiState.collectAsState()

    val isLoading = remember { derivedStateOf { uiState.value.isLoading } }
    if (isLoading.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        return
    }
    Column {
        TopAppBar(
            title = {
                Text(text = uiState.value.userName)
            },
            navigationIcon = {
                IconButton(
                    onClick = backPress
                ) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val basicInfoUiState = uiState.value
            item(
                key = "user_avatar"
            ) {
                NameAvatarImage(name = basicInfoUiState.userName, modifier = Modifier.size(56.dp))
            }
            item(
                key = "user_name"
            ) {
                Text(text = basicInfoUiState.userName, style = MaterialTheme.typography.titleMedium)
            }
            item(
                key = "operations"
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                ) {
                    FilledTonalButton(
                        onClick = {
                            navigateToChat(basicInfoUiState.sessionId, basicInfoUiState.sessionType)
                        }
                    ) {
                        Text(text = stringResource(Res.string.string_send_message_to_user))
                    }
                }
            }
        }
    }

}