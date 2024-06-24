package com.chat.compose.app.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.ui.framework.Box
import com.chat.compose.app.ui.framework.Column
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.app_name
import com.github.droidlin.composeapp.generated.resources.login_input_password
import com.github.droidlin.composeapp.generated.resources.login_input_username
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/6/25 00:19
 */
@Composable
fun LoginScreen() {
    val viewModel = koinViewModel<LoginViewModel>()
    val uiState = viewModel.state.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(Res.string.app_name), style = MaterialTheme.typography.displayMedium)
        Box {
            val inputText = remember { derivedStateOf { uiState.value.inputUserName } }
            OutlinedTextField(
                value = inputText.value,
                onValueChange = viewModel::updateUserName,
                maxLines = 1,
                singleLine = true,
                label = { Text(stringResource(Res.string.login_input_username)) },
                shape = MaterialTheme.shapes.medium
            )
        }
        Box {
            val inputText = remember { derivedStateOf { uiState.value.inputPassword } }
            val showRawPassword by remember { derivedStateOf { uiState.value.showRawPassword } }
            OutlinedTextField(
                value = inputText.value,
                onValueChange = viewModel::updatePassword,
                maxLines = 1,
                singleLine = true,
                label = { Text(stringResource(Res.string.login_input_password)) },
                shape = MaterialTheme.shapes.medium,
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.updateShowPassword(!showRawPassword) }
                    ) {
                        Icon(
                            imageVector = if (showRawPassword) {
                                Icons.Filled.CheckCircle
                            } else {
                                Icons.Filled.Check
                            },
                            contentDescription = "showRawPassword"
                        )
                    }
                },
                visualTransformation = if (showRawPassword) {
                    VisualTransformation.None
                } else PasswordVisualTransformation()
            )
        }
    }
}