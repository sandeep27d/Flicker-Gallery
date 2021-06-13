package com.sand.flickergalary.repo

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.sand.flickergalary.db.FavPhoto
import com.sand.flickergalary.db.FlickerDatabase
import com.sand.flickergalary.db.Photo
import com.sand.flickergalary.paging.FlickerRemoteMediator
import com.sand.flickergalary.api.FlickerApi
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class FlickerRepo(private val api: FlickerApi, private val database: FlickerDatabase) {
    fun getUpdatedFlow(query: String?): Flow<PagingData<Photo>> {
        return Pager(config = PagingConfig(pageSize = 60, prefetchDistance = 2),
                pagingSourceFactory = {  database.photoDao().allPhotos() },
                remoteMediator = FlickerRemoteMediator(query, database, api)
        ).flow
    }

    fun getUpdatedFavFlow(): LiveData<List<FavPhoto>> {
        return database.favPhotoDao().allPhotos()
    }
}
