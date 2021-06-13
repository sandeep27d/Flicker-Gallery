package com.sand.flickergalary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sand.flickergalary.databinding.AdapterItemPhotoBinding
import com.sand.flickergalary.db.FavPhoto
import com.sand.flickergalary.db.FavPhotoDao
import com.sand.flickergalary.db.FlickerDatabase
import com.sand.flickergalary.db.Photo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class FlickerAdapter(val clickListener: PhotoClickListener?) :
    PagingDataAdapter<Photo, FlickerAdapter.PhotoViewHolder>(Companion) {
    private var job: Job? = null
    private lateinit var favPhotoLV: LiveData<List<FavPhoto>>
    private lateinit var favPhotoDao: FavPhotoDao
    override fun onViewDetachedFromWindow(holder: PhotoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        job?.cancel()
    }

    companion object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            AdapterItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class PhotoViewHolder(private val binding: AdapterItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            if (!::favPhotoDao.isInitialized) {
                favPhotoDao = FlickerDatabase.getInstance(binding.root.context).favPhotoDao()
            }
            if (!::favPhotoLV.isInitialized) {
                favPhotoLV = favPhotoDao.allPhotos()
            }
        }

        fun bind(photo: Photo?) {
            photo?.isFav = if(favPhotoLV.value?.first { it.id == photo?.id } == null) 0 else 1
            binding.photo = photo
            binding.fav.setOnClickListener {
                photo?.isFav = if (photo?.isFav == 0) 1 else 0
                binding.photo = photo
                job?.cancel()
                job = GlobalScope.launch {
                    if (binding.photo?.isFav == 1) {
                        binding.photo?.toFavPhoto()?.let { favPhotoDao.insertAll(listOf(it)) }
                    } else {
                        binding.photo?.toFavPhoto()?.let { favPhotoDao.clear(it) }
                    }
                }
            }
            binding.root.setOnClickListener {
                clickListener?.onPhotoClick(photo)
            }
        }
    }

    interface PhotoClickListener {
        fun onPhotoClick(photo: Photo?)
    }
}