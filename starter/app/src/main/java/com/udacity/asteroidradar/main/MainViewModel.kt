package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.udacity.asteroidradar.database.AsteroidDao

class MainViewModel(
    val database: AsteroidDao,
    application: Application
) : AndroidViewModel(application) {
}