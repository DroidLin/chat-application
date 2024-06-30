package com.chat.compose.app.usecase.network

import com.chat.compose.app.network.ApiResult
import com.chat.compose.app.network.isSuccess
import com.chat.compose.app.network.serverRequest
import io.ktor.client.*
import io.ktor.http.*

/**
 * @author liuzhongao
 * @since 2024/6/30 12:34
 */
class RegistrationUseCase(private val httpClient: HttpClient) {

    suspend fun registration(userAccount: String, userName: String, password: String): ApiResult<Boolean> {
        val httpResponse = this.httpClient.serverRequest(
            path = "api/account/registration",
            parameters = Parameters.build {
                this["userAccount"] = userAccount
                this["userName"] = userName
                this["password"] = password
            }
        )
        return httpResponse.copy(data = httpResponse.isSuccess) as ApiResult<Boolean>
    }
}