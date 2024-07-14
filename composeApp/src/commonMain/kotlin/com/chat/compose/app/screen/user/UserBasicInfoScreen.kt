package com.chat.compose.app.screen.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.application.channel.message.SessionType
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.ui.NameAvatarImage
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.app_name
import com.github.droidlin.composeapp.generated.resources.string_send_message_to_user
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/7/12 00:23
 */
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