package com.sand.flickergalary.view_model

import androidx.paging.ExperimentalPagingApi
import com.sand.flickergalary.base.BaseViewModel
import com.sand.flickergalary.repo.FlickerRepo
import com.sand.flickergalary.ui.main.FlickerContract

@ExperimentalPagingApi
class FlickerFavViewModel(repo: FlickerRepo) :
    BaseViewModel<FlickerContract.Event, FlickerContract.State, FlickerContract.Effect>() {
    override fun createInitialState() = FlickerContract.State.Idle

    var favPhotos = repo.getUpdatedFavFlow()

    override fun handleEvent(event: FlickerContract.Event) {
    }
}