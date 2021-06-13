package com.sand.flickergalary.api

import com.sand.flickergalary.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickerApi {

    @GET("services/rest/")
    suspend fun getPagedResult(
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") apiKey: String = "062a6c0c49e4de1d78497d13a7dbb360",
        @Query("format") format: String = "json",
        @Query("nojsoncallback") callback: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("tags") tag: String,
        @Query("page") page: Int
    ) : SearchResponse
}
