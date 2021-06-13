package com.sand.flickergalary.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sand.flickergalary.R

@ExperimentalPagingApi
class FlickerActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_flicker_activity)

        navView = findViewById(R.id.navView)

        addNavItem(NavBarItem.HOME, 0)
        addNavItem(NavBarItem.FAV, 1)

    }

    private fun addNavItem(item: NavBarItem, index: Int) {
        navView.menu.add(Menu.NONE, item.destinationId, 0, getString(item.labelResourceId))
        navView.menu.findItem(item.destinationId).icon = ContextCompat.getDrawable(this, item.imgId)
    }

    override fun onStart() {
        super.onStart()
        navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.navView).setupWithNavController(navController)
    }
}