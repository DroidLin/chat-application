package com.chat.compose.app.usecase.network

import com.chat.compose.app.metadata.Profile
import com.chat.compose.app.network.ApiResult
import com.chat.compose.app.network.serverRequest
import com.chat.compose.app.util.fromJson
import io.ktor.client.*
import io.ktor.http.*

/**
 * @author liuzhongao
 * @since 2024/6/30 12:29
 */
class LoginUserUseCase(private val httpClient: HttpClient) {

    suspend fun loginUserAccount(userAccount: String, password: String): ApiResult<Profile> {
        val httpResponse = this.httpClient.serverRequest(
            path = "api/account/login",
            parameters = Parameters.build {
                this["userAccount"] = userAccount
                this["password"] = password
            }
        )
        val data = httpResponse.data?.toString()
        return (if (!data.isNullOrBlank()) {
            httpResponse.copy(data = fromJson<Profile>(data))
        } else httpResponse.copy(data = null)) as ApiResult<Profile>
    }
}