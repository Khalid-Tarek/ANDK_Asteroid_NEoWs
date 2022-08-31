package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.*
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

    private var _currentFilter = MutableLiveData<AsteroidRepository.AsteroidApiFilter>()
    val currentFilter: LiveData<AsteroidRepository.AsteroidApiFilter>
        get() = _currentFilter

    private val asteroidRepository = AsteroidRepository(database)
    val asteroids = asteroidRepository.asteroids

    init {
        getImageOfTheDay()
        _status.value = NASANEoWsApiStatus.SUCCESS
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

    fun loadAsteroids() {
        viewModelScope.launch {
            try {
                _status.value = NASANEoWsApiStatus.LOADING
                asteroidRepository.refreshAsteroids()
                _status.value = NASANEoWsApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.i(TAG, "Failure: $e")
                _status.value = NASANEoWsApiStatus.FAILURE
            }
        }
    }

    fun getFilteredList(): List<Asteroid> {
        val (startDate, endDate) = getQueryDates()
        return asteroids.value!!.filter {
            when (_currentFilter.value) {
                AsteroidRepository.AsteroidApiFilter.SHOW_WEEK -> it.closeApproachDate in startDate..endDate
                AsteroidRepository.AsteroidApiFilter.SHOW_TODAY -> it.closeApproachDate == startDate
                else -> true
            }
        }
    }

    fun setFilter(filter: AsteroidRepository.AsteroidApiFilter) {
        _currentFilter.value = filter
    }

}