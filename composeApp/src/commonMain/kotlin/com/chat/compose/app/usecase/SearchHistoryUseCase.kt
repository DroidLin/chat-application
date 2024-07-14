package com.chat.compose.app.usecase

import androidx.compose.runtime.Immutable
import com.chat.compose.app.util.PreferenceCenter
import com.chat.compose.app.util.fromJsonArray
import com.chat.compose.app.util.toJson
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/7/1 00:01
 */
class SearchHistoryUseCase {

    private val userPreference by PreferenceCenter.userBasicPreference
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.Default) }

    private val _historyConfigList = MutableStateFlow<List<SearchHistoryConfig>>(emptyList())

    init {
        this.coroutineScope.launch {
            val history = this@SearchHistoryUseCase.userPreference.getString(KEY_SEARCH_HISTORY, "")
            if (history.isNullOrBlank()) return@launch
            val historyConfigList = fromJsonArray<SearchHistoryConfig>(history)
            this@SearchHistoryUseCase._historyConfigList.update { historyConfigList ?: emptyList() }
        }
    }

    val historyConfigList = this._historyConfigList
        .map { historyConfigList ->
            if (historyConfigList.size >= 10) {
                historyConfigList.subList(0, 10)
            } else historyConfigList
        }
        .map { historyConfigList -> historyConfigList.sortedByDescending { it.timestamp }.distinctBy { it.value } }
        .distinctUntilChanged()

    fun insertKeywordHistory(keyword: String) {
        val historyConfigList = this._historyConfigList.value.toMutableList()
        historyConfigList += SearchHistoryConfig(keyword, System.currentTimeMillis())
        this.coroutineScope.launch {
            val distinctList = historyConfigList.sortedByDescending { it.timestamp }.distinctBy { it.value }
            this@SearchHistoryUseCase._historyConfigList.update { distinctList }
            this@SearchHistoryUseCase.userPreference.putString(KEY_SEARCH_HISTORY, distinctList.toJson())
            this@SearchHistoryUseCase.userPreference.flush()
        }
    }

    fun clearAllHistory() {
        this.coroutineScope.launch {
            this@SearchHistoryUseCase._historyConfigList.update { emptyList() }
            this@SearchHistoryUseCase.userPreference.putString(KEY_SEARCH_HISTORY, emptyList<SearchHistoryConfig>().toJson())
            this@SearchHistoryUseCase.userPreference.flush()
        }
    }

    companion object {
        private const val KEY_SEARCH_HISTORY = "KEY_SEARCH_HISTORY"
    }
}

@Immutable
@JsonClass(generateAdapter = true)
data class SearchHistoryConfig(
    val value: String = "",
    val timestamp: Long = 0
)