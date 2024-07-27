package com.chat.compose.app.screen.setting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import com.chat.compose.app.ui.NavRoute
import com.chat.compose.app.ui.framework.Column
import com.chat.compose.app.ui.navigationComposable
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_settings_title
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/6/16 21:44
 */
fun NavGraphBuilder.settingScreen(
    backPressed: () -> Unit
) {
    navigationComposable(
        route = NavRoute.Settings.route
    ) {
        SettingScreen(
            backPressed = backPressed
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    backPressed: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = stringResource(Res.string.string_settings_title)) },
            navigationIcon = {
                IconButton(onClick = { backPressed() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }
}