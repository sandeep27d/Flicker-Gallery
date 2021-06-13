package com.sand.flickergalary.ui.main

import com.sand.flickergalary.base.UiEffect
import com.sand.flickergalary.base.UiEvent
import com.sand.flickergalary.base.UiState

sealed class FlickerContract {
    sealed class Event : UiEvent {
        data class SearchQuery(val query: String?) : Event()
    }

    sealed class Effect : UiEffect {
    }

    sealed class State : UiState {
        object Idle : State()
        object SearchResultRefreshed : State()
    }
}