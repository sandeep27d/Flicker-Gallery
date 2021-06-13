package com.sand.flickergalary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sand.flickergalary.databinding.AdapterItemFavPhotoBinding
import com.sand.flickergalary.db.FavPhoto
import com.sand.flickergalary.db.FavPhotoDao
import com.sand.flickergalary.db.FlickerDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class FlickerFavAdapter(val clickListener: PhotoClickListener?) :
    ListAdapter<FavPhoto, FlickerFavAdapter.PhotoViewHolder>(Companion) {
    private var job: Job? = null
    private lateinit var favPhotoDao: FavPhotoDao

    override fun onViewDetachedFromWindow(holder: PhotoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        job?.cancel()
    }

    companion object : DiffUtil.ItemCallback<FavPhoto>() {
        override fun areItemsTheSame(oldItem: FavPhoto, newItem: FavPhoto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavPhoto, newItem: FavPhoto): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            AdapterItemFavPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class PhotoViewHolder(private val binding: AdapterItemFavPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            if (!::favPhotoDao.isInitialized) {
                favPhotoDao = FlickerDatabase.getInstance(binding.root.context).favPhotoDao()
            }
        }

        fun bind(photo: FavPhoto?) {
            binding.photo = photo
            binding.fav.setOnClickListener {
                photo?.isFav = if (photo?.isFav == 0) 1 else 0
                binding.photo = photo
                job?.cancel()
                job = GlobalScope.launch {
                    if (binding.photo?.isFav == 1) {
                        binding.photo?.let { favPhotoDao.insertAll(listOf(it)) }
                    } else {
                        binding.photo?.let { favPhotoDao.clear(it) }
                    }
                }
            }
            binding.root.setOnClickListener {
                clickListener?.onPhotoClick(photo)
            }
        }
    }

    interface PhotoClickListener {
        fun onPhotoClick(photo: FavPhoto?)
    }
}