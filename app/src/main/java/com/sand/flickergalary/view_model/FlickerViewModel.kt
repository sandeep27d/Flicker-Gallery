package com.sand.flickergalary.view_model

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.sand.flickergalary.StorageManager
import com.sand.flickergalary.base.BaseViewModel
import com.sand.flickergalary.repo.FlickerRepo
import com.sand.flickergalary.ui.main.FlickerContract
import com.sand.flickergalary.ui.main.FlickerContract.Event.SearchQuery
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class FlickerViewModel(private val repo: FlickerRepo) :
    BaseViewModel<FlickerContract.Event, FlickerContract.State, FlickerContract.Effect>() {
    override fun createInitialState() = FlickerContract.State.Idle

    companion object {
        private const val LAST_QUERY = "last_query"
        private const val DEFAULT_QUERY = "wallpaper"
    }

    var flow = repo.getUpdatedFlow(
        StorageManager.getString(LAST_QUERY)
            ?: DEFAULT_QUERY
    ).cachedIn(viewModelScope)

    override fun handleEvent(event: FlickerContract.Event) {
        when (event) {
            is SearchQuery -> {
                viewModelScope.launch {
                    flow = repo.getUpdatedFlow(
                        event.query ?: StorageManager.getString(LAST_QUERY)
                        ?: DEFAULT_QUERY
                    ).cachedIn(viewModelScope)
                    StorageManager.putString(LAST_QUERY, event.query)
                    setState { FlickerContract.State.SearchResultRefreshed(event.query) }
                }
            }
        }
    }
}