package com.chat.compose.app.usecase.network

import com.chat.compose.app.metadata.Profile
import com.chat.compose.app.network.serverRequest
import com.chat.compose.app.util.fromJson
import com.chat.compose.app.util.fromJsonArray
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author liuzhongao
 * @since 2024/6/29 18:04
 */
class FetchUserInfoUseCase(private val httpClient: HttpClient) {

    @JvmOverloads
    suspend fun fetchProfile(userId: Long? = null): Profile? {
        val response = this.httpClient.serverRequest(
            path = "api/userinfo/get",
            parameters = Parameters.build {
                if (userId != null) {
                    this["userId"] = userId.toString()
                }
            }
        )
        val userInfoDataString = response.data?.toString()
        if (userInfoDataString.isNullOrEmpty()) {
            return null
        }
        return withContext(Dispatchers.Default) { fromJson<Profile>(userInfoDataString) }
    }

    suspend fun fetchProfileList(ids: List<String>): List<Profile> {
        val response = this.httpClient.serverRequest(
            path = "api/userinfo/get",
            parameters = Parameters.build {
                this["userIdList"] = ids.joinToString(separator = ",")
            }
        )
        val userInfoListDataString = response.data?.toString()
        if (userInfoListDataString.isNullOrEmpty()) return emptyList()
        return withContext(Dispatchers.Default) {
            fromJsonArray<Profile>(userInfoListDataString)
        } ?: emptyList()
    }

    suspend fun fetchProfileBySessionId(sessionIdList: List<String>): List<Profile> {
        val response = this.httpClient.serverRequest(
            path = "api/userinfo/get",
            parameters = Parameters.build {
                this["sessionIdList"] = sessionIdList.joinToString(separator = ",")
            }
        )
        val userInfoListDataString = response.data?.toString()
        if (userInfoListDataString.isNullOrEmpty()) return emptyList()
        return withContext(Dispatchers.Default) {
            fromJsonArray<Profile>(userInfoListDataString)
        } ?: emptyList()
    }
}