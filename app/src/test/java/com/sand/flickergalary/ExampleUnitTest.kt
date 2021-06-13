package com.sand.flickergalary

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import com.sand.flickergalary.db.FlickerDatabase
import com.sand.flickergalary.db.Photo
import com.sand.flickergalary.api.FlickerApi
import com.sand.flickergalary.model.PhotoResponse
import com.sand.flickergalary.model.SearchResponse
import com.sand.flickergalary.paging.FlickerRemoteMediator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalPagingApi
class ExampleUnitTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    lateinit var database: FlickerDatabase

    lateinit var api: FlickerApi

    lateinit var remoteMediator: FlickerRemoteMediator


    @Before
    fun setup() {
        api = object : FlickerApi {
            override suspend fun getPagedResult(
                method: String,
                apiKey: String,
                format: String,
                callback: Int,
                perPage: Int,
                tag: String,
                page: Int
            ): SearchResponse {
                return SearchResponse(
                    photoResponse = PhotoResponse(
                        page = 1,
                        pages = 10,
                        perPage = 2,
                        total = "40",
                        photos = listOf(Photo(id = "1"), Photo(id = "2"))
                    )
                )
            }
        }

        database = FlickerDatabase.getInstance(mock<Context>())
        remoteMediator = FlickerRemoteMediator("query", database, api)

    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        val pagingState = PagingState<Int, Photo>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is MediatorResult.Success)
//        assertFalse { (result as MediatorResult.Success).endOfPaginationReached }
    }
}