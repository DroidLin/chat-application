package com.chat.compose.app.screen.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.router.LocalRouteAction
import com.chat.compose.app.router.navigateToSettings
import com.chat.compose.app.ui.NameAvatarImage
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.framework.Box
import com.chat.compose.app.ui.framework.Column
import com.chat.compose.app.ui.framework.Row
import com.chat.compose.app.ui.navigationComposable
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_personal_info_title
import org.jetbrains.compose.resources.stringResource

fun NavGraphBuilder.personalInformationScreen(
    navigateToSetting: () -> Unit
) {
    navigationComposable(
        route = NavRoute.PersonalInfo.route
    ) {
        PersonalInformationScreen(
            navigateToSetting = navigateToSetting
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInformationScreen(
    navigateToSetting: () -> Unit
) {
    val viewModel = koinViewModel<PersonalInfoViewModel>()
    val uiState = viewModel.uiState.collectAsState()
    val userInfo = viewModel.userProfile.collectAsState()

    Column {
        TopAppBar(
            title = {
                Text(stringResource(Res.string.string_personal_info_title))
            },
            actions = {
                IconButton(
                    onClick = navigateToSetting
                ) {
                    Icon(Icons.Default.Settings, null)
                }
            }
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item(
                key = "personal_info_header"
            ) {
                Box(
                    modifier = Modifier.fillParentMaxWidth().padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val userName by remember { derivedStateOf { userInfo.value.userInfo } }
                        NameAvatarImage(
                            name = userName?.userName ?: "N",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = userName?.userName ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            item(
                key = "personal_info_item_logout"
            ) {
                Button(
                    onClick = {

                    }
                ) {

                }
            }
        }
    }
}