package com.chat.compose.app.usecase.network

import com.chat.compose.app.metadata.SearchComplexResource
import com.chat.compose.app.network.serverRequest
import com.chat.compose.app.util.fromJson
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

/**
 * @author liuzhongao
 * @since 2024/7/4 23:52
 */
class SearchComplexUseCase(private val httpClient: HttpClient) {

    fun searchComplexResourceFlow(query: String) = flow {
        val searchComplexResource = this@SearchComplexUseCase.searchComplex(query)
        emit(searchComplexResource)
    }

    suspend fun searchComplex(query: String): SearchComplexResource? {
        val apiResult = this.httpClient.serverRequest(
            path = "api/search/v1/complex",
            parameters = Parameters.build {
                this["query"] = query
            }
        )

        val jsonString = apiResult.data?.toString()
        if (jsonString.isNullOrEmpty()) return null

        return kotlin.runCatching { fromJson<SearchComplexResource>(jsonString) }
            .onFailure { it.printStackTrace() }
            .getOrNull()
    }
}