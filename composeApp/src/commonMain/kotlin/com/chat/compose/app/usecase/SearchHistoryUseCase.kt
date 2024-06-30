package com.chat.compose.app.usecase

import androidx.compose.runtime.Immutable
import com.chat.compose.app.util.PreferenceCenter
import com.chat.compose.app.util.fromJsonArray
import com.chat.compose.app.util.toJson
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * @author liuzhongao
 * @since 2024/7/1 00:01
 */
class SearchHistoryUseCase {

    private val userPreference by PreferenceCenter.userBasicPreference
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.Default) }

    val historyConfigList get() = flow {
        val history = this@SearchHistoryUseCase.userPreference.getString(KEY_SEARCH_HISTORY, "")
        if (history.isNullOrBlank()) {
            emit(emptyList())
            return@flow
        }
        val historyConfigList = fromJsonArray<SearchHistoryConfig>(history)
        emit(historyConfigList ?: emptyList())
    }
        .distinctUntilChanged()
        .stateIn(this.coroutineScope, SharingStarted.Lazily, emptyList())

    suspend fun insertKeywordHistory(keyword: String) {
        val historyConfigList = this.historyConfigList.value.toMutableList()
        historyConfigList += SearchHistoryConfig(keyword, System.currentTimeMillis())
        withContext(this.coroutineScope.coroutineContext) {
            this@SearchHistoryUseCase.userPreference.putString(KEY_SEARCH_HISTORY, historyConfigList.toJson())
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