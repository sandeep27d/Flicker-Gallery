package com.sand.flickergalary.ui.fav

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.sand.flickergalary.view_model.FlickerFavViewModel
import com.sand.flickergalary.ui.image_viewer.ImageViewerDialog
import com.sand.flickergalary.R
import com.sand.flickergalary.ServiceGenerator
import com.sand.flickergalary.base.BaseFragment
import com.sand.flickergalary.databinding.LayoutFavFragmentBinding
import com.sand.flickergalary.api.FlickerApi
import com.sand.flickergalary.adapter.FlickerFavAdapter
import com.sand.flickergalary.repo.FlickerRepo
import com.sand.flickergalary.db.FavPhoto
import com.sand.flickergalary.db.FlickerDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class FavFragment: BaseFragment<LayoutFavFragmentBinding, FlickerFavViewModel>() {

    private var job: Job? = null
    private val favListener = object: FlickerFavAdapter.PhotoClickListener{
        override fun onPhotoClick(photo: FavPhoto?) {
            photo?.let{ ImageViewerDialog().show(photo, parentFragmentManager)}
        }
    }
    private val photoAdapter by lazy { FlickerFavAdapter(favListener) }

    override fun contentLayout() = R.layout.layout_fav_fragment

    override fun createCustomVm(): FlickerFavViewModel {
        return FlickerFavViewModel(FlickerRepo(ServiceGenerator.generateService(FlickerApi::class.java),
            FlickerDatabase.getInstance(requireContext()))
        )
    }

    override fun onViewReady(
        binding: LayoutFavFragmentBinding,
        vm: FlickerFavViewModel,
        savedInstanceState: Bundle?
    ) {
        setupUi()
        collectPagedData()
    }

    private fun setupUi() {
        binding.photoList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.photoList.setHasFixedSize(true)
        binding.photoList.adapter = photoAdapter
        binding.photoList.requestFocus()
    }

    private fun collectPagedData() {
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favPhotos.observe(viewLifecycleOwner, Observer {
                photoAdapter.submitList(it)
            })
        }
    }
}