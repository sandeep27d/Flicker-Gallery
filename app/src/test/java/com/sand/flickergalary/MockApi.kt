package com.sand.flickergalary

import com.sand.flickergalary.api.FlickerApi
import com.sand.flickergalary.db.Photo
import com.sand.flickergalary.model.PhotoResponse
import com.sand.flickergalary.model.SearchResponse

class MockApi {
    companion object {
        val Success = SearchResponse(
            photoResponse = PhotoResponse(
                page = 1,
                pages = 10,
                perPage = 2,
                total = "40",
                photos = listOf(Photo(id = "1"), Photo(id = "2"))
            )
        )

        val Empty = SearchResponse(
            photoResponse = PhotoResponse(
                page = 1,
                pages = 10,
                perPage = 2,
                total = "40",
                photos = listOf()
            )
        )

        fun create(function: () -> SearchResponse): FlickerApi {
            return object : FlickerApi{
                override suspend fun getPagedResult(
                    method: String,
                    apiKey: String,
                    format: String,
                    callback: Int,
                    perPage: Int,
                    tag: String,
                    page: Int
                ): SearchResponse {
                    return function.invoke()
                }
            }
        }
    }
}
