package com.chat.compose.app.usecase.network

import com.chat.compose.app.network.ApiResult
import com.chat.compose.app.network.isSuccess
import com.chat.compose.app.network.serverRequest
import io.ktor.client.*
import io.ktor.http.*

/**
 * @author liuzhongao
 * @since 2024/6/30 12:38
 */
class UserAccountCheckForRegistrationUseCase(private val httpClient: HttpClient) {

    suspend fun checkUseAccountAvailable(userAccount: String): ApiResult<Boolean> {
        val httpResponse = this.httpClient.serverRequest(
            path = "api/account/user/check",
            parameters = Parameters.build {
                this["userAccount"] = userAccount
            }
        )
        return httpResponse.copy(data = httpResponse.isSuccess) as ApiResult<Boolean>
    }
}