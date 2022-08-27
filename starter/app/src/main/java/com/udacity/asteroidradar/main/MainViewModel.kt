package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDao
import kotlinx.coroutines.launch

class MainViewModel(
    val database: AsteroidDao,
    application: Application
) : AndroidViewModel(application) {

    private val _asteroids = database.getAllAsteroids()
    val asteroids
        get() = _asteroids

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()
    val navigateToAsteroid: LiveData<Asteroid>
        get() = _navigateToAsteroid

    fun getAsteroid(id: Long): Asteroid? {
        var asteroid: Asteroid? = null
        viewModelScope.launch {
            asteroid = database.get(id)
        }
        return asteroid
    }

    fun deleteOldAsteroids() {
        viewModelScope.launch {
            database.deleteOldAsteroids()
        }
    }

    fun insertAsteroid(asteroid: Asteroid) {
        viewModelScope.launch {
            database.insert(asteroid)
        }
    }

    fun doneNavigating() {
        _navigateToAsteroid.value = null
    }

    fun navigateToAsteroid(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }
}