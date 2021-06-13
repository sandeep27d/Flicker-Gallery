package com.sand.flickergalary.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import com.sand.flickergalary.R

enum class NavBarItem(val labelResourceId: Int, @DrawableRes val imgId: Int, @MenuRes val destinationId: Int) {
    HOME(R.string.home, R.drawable.ic_baseline_home_24, R.id.gallery_dest),
    FAV(R.string.favorite, R.drawable.ic_baseline_star_24, R.id.fav_dest),
}