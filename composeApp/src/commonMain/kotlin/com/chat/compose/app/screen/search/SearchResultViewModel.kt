package com.chat.compose.app.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.compose.app.metadata.isValid
import com.chat.compose.app.usecase.network.SearchComplexUseCase
import kotlinx.coroutines.flow.*

/**
 * @author liuzhongao
 * @since 2024/7/11 00:19
 */
class SearchResultViewModel(
    private val searchComplexUseCase: SearchComplexUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchResultUiState())
    val uiState = this._uiState.asStateFlow()

    fun searchComplexFlow(query: String) = this.searchComplexUseCase.searchComplexResourceFlow(query)
        .map { resource ->
            resource?.copy(userInfo = resource.userInfo.filter { it.isValid })
        }
        .stateIn(this.viewModelScope, SharingStarted.Lazily, null)
}

data class SearchResultUiState(
    val isLoading: Boolean = false,
)