package com.sand.flickergalary.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Photo>)

    @Query("SELECT * FROM photos")
    fun allPhotos(): PagingSource<Int, Photo>

    @Query("DELETE FROM photos")
    suspend fun clearAll()
}

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remotekeys WHERE photoId = :id")
    suspend fun remoteKeysPhotoId(id: String): RemoteKeys?

    @Query("DELETE FROM remotekeys")
    suspend fun clearRemoteKeys()
}

@Dao
interface FavPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<FavPhoto>)

    @Query("SELECT * FROM fav_photos")
    fun allPhotos(): LiveData<List<FavPhoto>>

    @Delete()
    suspend fun clear(photo: FavPhoto)
}
