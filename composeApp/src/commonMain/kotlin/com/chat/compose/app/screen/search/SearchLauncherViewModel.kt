package com.chat.compose.app.screen.search

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.chat.compose.app.usecase.SearchHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author liuzhongao
 * @since 2024/7/1 00:00
 */
class SearchLauncherViewModel(
    private val searchHistoryUserCase: SearchHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchLauncherUiState())
    val uiState = this._uiState.asStateFlow()

    val historyConfig by lazy { this.searchHistoryUserCase.historyConfigList }

    fun onInputChange(newValue: String) {
        this._uiState.update { it.copy(searchInputKeyword = newValue) }
    }

    fun onSearch() {

    }
}

@Immutable
data class SearchLauncherUiState(
    val searchInputKeyword: String = "",
)