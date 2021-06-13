package com.sand.flickergalary.base

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sand.flickergalary.db.Photo

@BindingAdapter("app:photo")
fun setImage(view: AppCompatImageView, photo: Photo?) {
    Glide.with(view)
        .load(photo?.uri)
        .into(view)
}

@BindingAdapter("app:photoHigh")
fun setImageHigh(view: AppCompatImageView, photo: Photo?) {
    Glide.with(view)
        .load(photo?.uri_medium)
        .thumbnail(.25f)
        .into(view)
}