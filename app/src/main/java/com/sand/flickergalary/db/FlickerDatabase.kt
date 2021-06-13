package com.sand.flickergalary.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [Photo::class, RemoteKeys::class, FavPhoto::class], version = 1, exportSchema = false)
abstract class FlickerDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun favPhotoDao(): FavPhotoDao

    companion object {
        private const val FLICKER_DB = "flicker.db"

        @Volatile
        private var INSTANCE: FlickerDatabase? = null

        fun getInstance(context: Context): FlickerDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context, FlickerDatabase::class.java, FLICKER_DB)
                        .setTransactionExecutor(Executors.newSingleThreadExecutor())
                        .build()
    }

}
