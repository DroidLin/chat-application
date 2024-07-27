package com.chat.compose.app.screen.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.chat.compose.app.di.koinViewModel
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.framework.Box
import com.chat.compose.app.ui.framework.Column
import com.chat.compose.app.ui.homeNavigationComposable
import com.github.droidlin.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/6/25 00:19
 */
fun NavGraphBuilder.loginScreen(loginComplete: () -> Unit) {
    homeNavigationComposable(
        route = NavRoute.LoginRoute.Login.route
    ) {
        LoginScreen(loginComplete)
    }
}


@Composable
fun LoginScreen(
    loginComplete: () -> Unit,
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    val launchLogin = {
        viewModel.launchLogin(loginComplete)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(Res.string.app_name), style = MaterialTheme.typography.displayMedium)
            Box {
                val inputTextState by remember { derivedStateOf { uiState.value.inputUserName } }
                val inputText by remember { derivedStateOf { inputTextState.inputText } }
                val isError by remember { derivedStateOf { inputTextState.isError } }
                OutlinedTextField(
                    value = inputText,
                    onValueChange = viewModel::updateUserName,
                    maxLines = 1,
                    singleLine = true,
                    label = {
                        val errorMessage by remember { derivedStateOf { inputTextState.errorMessage } }
                        val showingContent = if (isError) {
                            errorMessage
                        } else stringResource(Res.string.login_input_username)
                        Text(showingContent)
                    },
                    shape = MaterialTheme.shapes.medium
                )
            }
            Box {
                val inputTextState by remember { derivedStateOf { uiState.value.inputPassword } }
                val inputText by remember { derivedStateOf { inputTextState.inputText } }
                val isError by remember { derivedStateOf { inputTextState.isError } }
                val showRawPassword by remember { derivedStateOf { uiState.value.showRawPassword } }
                OutlinedTextField(
                    value = inputText,
                    onValueChange = viewModel::updatePassword,
                    isError = isError,
                    maxLines = 1,
                    singleLine = true,
                    label = {
                        val errorMessage by remember { derivedStateOf { inputTextState.errorMessage } }
                        val showingContent = if (isError) {
                            errorMessage
                        } else stringResource(Res.string.login_input_password)
                        Text(showingContent)
                    },
                    shape = MaterialTheme.shapes.medium,
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.updateShowPassword(!showRawPassword) }
                        ) {
                            Icon(
                                painter = if (showRawPassword) {
                                    painterResource(Res.drawable.ic_eye_open)
                                } else {
                                    painterResource(Res.drawable.ic_eye_close)
                                },
                                contentDescription = "showRawPassword"
                            )
                        }
                    },
                    visualTransformation = if (showRawPassword) {
                        VisualTransformation.None
                    } else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onDone = { launchLogin() })
                )
            }
            LoginBottomCustomArea(modifier = Modifier)
            Box {
                val isLoadingState = remember { derivedStateOf { uiState.value.isLoading } }
                val buttonEnable by remember {
                    derivedStateOf {
                        val isLoading = uiState.value.isLoading
                        val isUserNameInputNotEmpty = uiState.value.inputUserName.inputText.isNotEmpty()
                        val isPasswordInputNotEmpty = uiState.value.inputPassword.inputText.isNotEmpty()
                        val isUserNameInputError = uiState.value.inputUserName.isError
                        val isPasswordInputError = uiState.value.inputUserName.isError
                        !isLoading && isUserNameInputNotEmpty && isPasswordInputNotEmpty && !isUserNameInputError && !isPasswordInputError
                    }
                }
                Button(
                    onClick = launchLogin,
                    enabled = buttonEnable,
                ) {
                    AnimatedContent(isLoadingState.value) { isLoading ->
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 3.dp)
                        } else {
                            Text(text = stringResource(Res.string.login_title))
                        }
                    }
                }
            }
        }
    }
}

@Composable
expect fun LoginBottomCustomArea(modifier: Modifier)