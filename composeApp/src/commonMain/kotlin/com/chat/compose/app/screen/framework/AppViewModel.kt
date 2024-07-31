package com.chat.compose.app.screen.framework

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.chat.compose.app.platform.viewmodel.AbstractStatefulViewModel
import com.chat.compose.app.platform.viewmodel.Event
import com.chat.compose.app.platform.viewmodel.State
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.string_navigation_message_label
import com.github.droidlin.composeapp.generated.resources.string_navigation_personal_info_label
import org.jetbrains.compose.resources.StringResource

/**
 * @author liuzhongao
 * @since 2024/7/21 19:13
 */
class AppViewModel : AbstractStatefulViewModel<AppUiState, AppEvent>() {

    override val initialState: AppUiState get() = AppUiState()

    fun switchNavigationTo(enum: AppHomeEnum) {
        this.updateState { it.copy(homeEnum = enum) }
    }
}

enum class AppHomeEnum(
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

interface AppEvent : Event

data class AppUiState(
    val homeEnum: AppHomeEnum = AppHomeEnum.Message,
) : State.Success