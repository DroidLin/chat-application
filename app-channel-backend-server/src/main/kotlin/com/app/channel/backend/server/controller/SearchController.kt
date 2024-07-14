package com.app.channel.backend.server.controller

import com.app.channel.backend.server.metadata.ApiResult
import com.app.channel.backend.server.service.SearchService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author liuzhongao
 * @since 2024/7/2 00:16
 */
@RestController
@RequestMapping("/api/search/v1")
class SearchController(
    private val searchService: SearchService
) {

    @RequestMapping(
        value = ["/complex"],
        method = [RequestMethod.GET, RequestMethod.POST],
    )
    suspend fun searchComplex(
        @RequestParam("query", required = false) query: String?,
    ): ApiResult<out Any> {
        val searchResult = this.searchService.searchComplex(query)
        return ApiResult.success(searchResult)
    }
}