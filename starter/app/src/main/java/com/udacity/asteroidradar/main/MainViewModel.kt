package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.AsteroidRepository
import com.udacity.asteroidradar.api.NASANEoWsApi
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.api.parseImageOfTheDay
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.await

enum class NASANEoWsApiStatus { LOADING, FAILURE, SUCCESS }

class MainViewModel(
    val database: AsteroidDatabase,
    application: Application
) : AndroidViewModel(application) {

    private val TAG = "MainViewModel"

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()
    val navigateToAsteroid: LiveData<Asteroid>
        get() = _navigateToAsteroid

    private val _imageOfTheDay = MutableLiveData<PictureOfDay>()
    val imageOfTheDay: LiveData<PictureOfDay>
        get() = _imageOfTheDay

    private val _status = MutableLiveData<NASANEoWsApiStatus>()
    val status: LiveData<NASANEoWsApiStatus>
        get() = _status

    private val asteroidRepository = AsteroidRepository(database)
    val asteroids = asteroidRepository.asteroids

    init {
        getImageOfTheDay()

        _status.value = if (asteroids.value?.isEmpty() == true)
                            NASANEoWsApiStatus.FAILURE
                        else
                            NASANEoWsApiStatus.SUCCESS
    }

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            try {
                val response = NASANEoWsApi.retrofitService.getImageOfTheDay().await()
                _imageOfTheDay.value = parseImageOfTheDay(JSONObject(response))
            } catch (e: Exception) {
                Log.i(TAG, "Failure: $e")
            }
        }
    }

    fun doneNavigating() {
        _navigateToAsteroid.value = null
    }

    fun navigateToAsteroid(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

}