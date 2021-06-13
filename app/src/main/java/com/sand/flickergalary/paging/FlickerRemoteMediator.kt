package com.sand.flickergalary.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sand.flickergalary.db.FlickerDatabase
import com.sand.flickergalary.db.Photo
import com.sand.flickergalary.db.RemoteKeys
import com.sand.flickergalary.api.FlickerApi

@OptIn(ExperimentalPagingApi::class)
class FlickerRemoteMediator(
        private val query: String?,
        private val database: FlickerDatabase,
        private val api: FlickerApi
) : RemoteMediator<Int, Photo>() {
    companion object {
        private const val DEFAULT_PAGE_INDEX = 1
    }

    override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, Photo>
    ): MediatorResult {
        if (query.isNullOrEmpty()) {
            return MediatorResult.Success(true)
        }
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = api.getPagedResult(
                    perPage = state.config.pageSize,
                    tag = query,
                    page = page,
            )
            val isEndOfList = response.photoResponse?.photos?.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.photoDao().clearAll()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList == true) null else page + 1
                val keys = response.photoResponse?.photos?.map {
                    RemoteKeys(photoId = it.id, curKey = page, prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { database.remoteKeysDao().insertAll(it) }
                response.photoResponse?.photos?.let { database.photoDao().insertAll(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList != false)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Photo>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.curKey ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                remoteKeys?.nextKey ?: DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Photo>): RemoteKeys? {
        return state.pages
                .lastOrNull { it.data.isNotEmpty() }
                ?.data?.lastOrNull()
                ?.let { photo -> database.remoteKeysDao().remoteKeysPhotoId(photo.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Photo>): RemoteKeys? {
        return state.pages
                .firstOrNull { it.data.isNotEmpty() }
                ?.data?.firstOrNull()
                ?.let { photo -> database.remoteKeysDao().remoteKeysPhotoId(photo.id) }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, Photo>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeysDao().remoteKeysPhotoId(repoId)
            }
        }
    }
}
