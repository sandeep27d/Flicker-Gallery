package com.sand.flickergalary.ui.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.sand.flickergalary.R
import com.sand.flickergalary.ServiceGenerator
import com.sand.flickergalary.adapter.FlickerAdapter
import com.sand.flickergalary.api.FlickerApi
import com.sand.flickergalary.base.BaseFragment
import com.sand.flickergalary.databinding.LayoutFlickerFragmentBinding
import com.sand.flickergalary.db.FlickerDatabase
import com.sand.flickergalary.db.Photo
import com.sand.flickergalary.repo.FlickerRepo
import com.sand.flickergalary.ui.image_viewer.ImageViewerDialog
import com.sand.flickergalary.view_model.FlickerViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@ExperimentalPagingApi
class FlickerFragment : BaseFragment<LayoutFlickerFragmentBinding, FlickerViewModel>() {

    private var job: Job? = null
    private val listener = object: FlickerAdapter.PhotoClickListener{
        override fun onPhotoClick(photo: Photo?) {
            photo?.let{ ImageViewerDialog().show(photo, parentFragmentManager)}
        }
    }
    private val photoAdapter by lazy { FlickerAdapter(listener) }

    override fun contentLayout() = R.layout.layout_flicker_fragment

    override fun createCustomVm(): FlickerViewModel? {
        return FlickerViewModel(
            FlickerRepo(ServiceGenerator.generateService(FlickerApi::class.java),
                FlickerDatabase.getInstance(requireContext()))
        )
    }

    override fun onViewReady(
        binding: LayoutFlickerFragmentBinding,
        vm: FlickerViewModel,
        savedInstanceState: Bundle?
    ) {
        setupUi()
        setupListener()
        collectPagedData()
        collectState()
    }

    private fun setupUi() {
        binding.photoList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.photoList.setHasFixedSize(true)
        binding.photoList.adapter = photoAdapter
        binding.photoList.requestFocus()
    }

    private fun setupListener() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setEvent(FlickerContract.Event.SearchQuery(query))
                closeKeyboard()
                binding.photoList.scrollToPosition(0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun collectPagedData() {
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { data ->
                photoAdapter.submitData(data)
            }
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                when(it) {
                    FlickerContract.State.Idle -> {}
                    FlickerContract.State.SearchResultRefreshed -> {
                        job?.cancel()
                        collectPagedData()
                    }
                }
            }
        }
    }


    private fun closeKeyboard() {
        val view: View? = requireActivity().currentFocus
        if (view != null) {
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}