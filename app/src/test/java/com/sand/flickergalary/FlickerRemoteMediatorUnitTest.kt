package com.sand.flickergalary

import android.content.Context
import android.os.Build.VERSION_CODES.P
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sand.flickergalary.api.FlickerApi
import com.sand.flickergalary.db.FlickerDatabase
import com.sand.flickergalary.db.Photo
import com.sand.flickergalary.paging.FlickerRemoteMediator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.concurrent.Executors

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [P])
class FlickerRemoteMediatorUnitTest {


    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var database: FlickerDatabase

    private lateinit var api: FlickerApi

    private lateinit var remoteMediator: FlickerRemoteMediator

    private lateinit var pagingState: PagingState<Int, Photo>

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, FlickerDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()

        pagingState = PagingState(listOf(), null, PagingConfig(10), 10)
    }

    @Test
    fun `refresh load return success result when more data is present`() = runBlocking {
        remoteMediator =
            FlickerRemoteMediator("query", database, MockApi.create { MockApi.Success })
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is MediatorResult.Success)
        assertFalse((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `refresh load return success with end reached as true result when no data is present`() = runBlocking {
        remoteMediator =
            FlickerRemoteMediator("query", database, MockApi.create { MockApi.Empty })
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `append load returns success with end reached as false when more data present`() = runBlocking {
        remoteMediator =
            FlickerRemoteMediator("query", database, MockApi.create { MockApi.Success })
        val result = remoteMediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is MediatorResult.Success)
        assertFalse((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `append load returns success with end reached as true when no more data present`() = runBlocking {
        remoteMediator =
            FlickerRemoteMediator("query", database, MockApi.create { MockApi.Empty })
        val result = remoteMediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `prepend load returns success with end reached as true when no more data present`() = runBlocking {
        remoteMediator =
            FlickerRemoteMediator("query", database, MockApi.create { MockApi.Empty })
        val result = remoteMediator.load(LoadType.PREPEND, pagingState)
        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
    }
}