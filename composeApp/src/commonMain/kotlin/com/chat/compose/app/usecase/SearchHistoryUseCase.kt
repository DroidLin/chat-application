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
import kotlinx.coroutines.withContext

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
        .map { historyConfigList -> historyConfigList.sortedByDescending { it.timestamp }.distinctBy { it.value } }
        .map { historyConfigList ->
            if (historyConfigList.size >= 10) {
                historyConfigList.subList(0, 10)
            } else historyConfigList
        }
        .distinctUntilChanged()

    fun insertKeywordHistory(keyword: String) {
        updateSearchHistoryConfig {
            this += SearchHistoryConfig(keyword, System.currentTimeMillis())
        }
    }

    fun deleteHistoryConfig(historyConfig: SearchHistoryConfig) {
        updateSearchHistoryConfig {
            this -= historyConfig
        }
    }

    private fun updateSearchHistoryConfig(function: MutableList<SearchHistoryConfig>.() -> Unit) {
        this.coroutineScope.launch {
            this@SearchHistoryUseCase._historyConfigList.update { historyConfigList ->
                historyConfigList.toMutableList()
                    .apply(function)
                    .also { mutableList ->
                        withContext(Dispatchers.IO) {
                            this@SearchHistoryUseCase.userPreference.putString(KEY_SEARCH_HISTORY, mutableList.toJson())
                            this@SearchHistoryUseCase.userPreference.flush()
                        }
                    }
            }
        }
    }

    fun clearAllHistory() {
        updateSearchHistoryConfig { clear() }
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