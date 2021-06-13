package com.sand.flickergalary

import android.app.Application

class FlickerApp: Application() {
    init {
        instance = this
    }
    companion object{
        lateinit var instance: FlickerApp
    }
}