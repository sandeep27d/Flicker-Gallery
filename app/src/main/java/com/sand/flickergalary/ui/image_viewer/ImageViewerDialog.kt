package com.sand.flickergalary.ui.image_viewer

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.sand.flickergalary.R
import com.sand.flickergalary.databinding.FragmentImageViewerBinding
import com.sand.flickergalary.db.FavPhoto
import com.sand.flickergalary.db.Photo

class ImageViewerDialog: DialogFragment() {

    private var initialState: Photo? = null
    private lateinit var binding: FragmentImageViewerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_viewer, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initialState?.let { binding.photo = it }
        isCancelable = true
        binding.bg.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawableResource(R.color.overlay)
        }
    }

    override fun getTheme(): Int = R.style.DialogTheme

    fun show(photo:Photo, manager: FragmentManager){
        show(manager, tag)
        if (::binding.isInitialized) {
            binding.photo = photo
        } else {
            initialState = photo
        }
    }

    fun show(photo:FavPhoto, manager: FragmentManager){
        show(photo.toPhoto(), manager)
    }
}