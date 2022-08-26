package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDao
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val dataSource: AsteroidDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(dataSource, application) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}