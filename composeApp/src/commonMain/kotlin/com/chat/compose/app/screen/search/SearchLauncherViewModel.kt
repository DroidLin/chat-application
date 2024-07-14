package com.chat.compose.app.screen.search

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.compose.app.usecase.SearchHistoryUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/7/1 00:00
 */
class SearchLauncherViewModel(
    private val searchHistoryUserCase: SearchHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchLauncherUiState())
    val uiState = this._uiState.asStateFlow()

    val historyConfig = this.searchHistoryUserCase.historyConfigList
        .stateIn(this.viewModelScope, SharingStarted.Lazily, emptyList())

    fun onInputChange(newValue: String) {
        this._uiState.update { it.copy(searchInputKeyword = newValue) }
    }

    fun onSearch(keyword: String? = null) {
        val input = keyword ?: this._uiState.value.searchInputKeyword
        if (input.isEmpty()) return
        this.searchHistoryUserCase.insertKeywordHistory(input)
    }

    fun clearAllHistory() {
        this.searchHistoryUserCase.clearAllHistory()
    }
}

@Immutable
data class SearchLauncherUiState(
    val searchInputKeyword: String = "",
)