package com.sand.flickergalary.model

import com.google.gson.annotations.SerializedName
import com.sand.flickergalary.db.Photo

data class SearchResponse(
    @SerializedName("photos") val photoResponse: PhotoResponse? = null,
    @SerializedName("stat") val stat: String? = null
)

data class PhotoResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("pages") val pages: Int? = null,
    @SerializedName("perpage") val perPage: Int? = null,
    @SerializedName("total") val total: String? = null,
    @SerializedName("photo") val photos: List<Photo>? = null
)
