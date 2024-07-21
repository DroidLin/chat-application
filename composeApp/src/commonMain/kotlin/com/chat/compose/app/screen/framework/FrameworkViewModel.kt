package com.chat.compose.app.screen.framework

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_navigation_message_label
import com.github.droidlin.composeapp.generated.resources.string_navigation_personal_info_label
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource

/**
 * @author liuzhongao
 * @since 2024/7/21 19:13
 */
class FrameworkViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FrameworkUiState())
    val uiState = this._uiState.asStateFlow()

    fun switchNavigationTo(enum: FrameworkHomeEnum) {
        this._uiState.update { it.copy(homeEnum = enum) }
    }
}

enum class FrameworkHomeEnum(
    val icon: ImageVector,
    val title: StringResource
) {
    Message(
        icon = Icons.Filled.Email,
        title = Res.string.string_navigation_message_label,
    ),
    Personal(
        icon = Icons.Filled.Person,
        title = Res.string.string_navigation_personal_info_label,
    );
}

data class FrameworkUiState(
    val homeEnum: FrameworkHomeEnum = FrameworkHomeEnum.Message,
)