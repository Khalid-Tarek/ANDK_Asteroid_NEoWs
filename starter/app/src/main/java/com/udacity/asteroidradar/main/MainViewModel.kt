package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NASANEoWsApi
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parseImageOfTheDay
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDao
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    val database: AsteroidDao,
    application: Application
) : AndroidViewModel(application) {

    private val TAG = "MainViewModel"

    //private val _asteroids = database.getAllAsteroids()
    private val _asteroids = MutableLiveData<MutableList<Asteroid>>()
    val asteroids: LiveData<MutableList<Asteroid>>
        get() = _asteroids

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()
    val navigateToAsteroid: LiveData<Asteroid>
        get() = _navigateToAsteroid

    private val _imageOfTheDay = MutableLiveData<PictureOfDay>()
    val imageOfTheDay: LiveData<PictureOfDay>
        get() = _imageOfTheDay

    private val _errorState = MutableLiveData<Throwable>()
    val errorState: LiveData<Throwable>
        get() = _errorState

    init {
        getImageOfTheDay()
        getAsteroids()
    }

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            try {
                val response = NASANEoWsApi.retrofitService.getImageOfTheDay().await()
                _imageOfTheDay.value = parseImageOfTheDay(JSONObject(response))
            } catch (e: Exception) {
                _errorState.value = e
            }
        }
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                val (startDate, endDate) = getQueryDates()
                val response = NASANEoWsApi.retrofitService.getAsteroids(startDate, endDate).await()
                _asteroids.value = parseAsteroidsJsonResult(JSONObject(response))
            } catch (e: Exception) {
                Log.i(TAG, "Failure: $e")
                _errorState.value = e
            }
        }
    }

    private fun getQueryDates(): Pair<String, String> {
        val dateToday = Calendar.getInstance()
        val dateAfterDefaultPeriod = dateToday.clone() as Calendar
        dateAfterDefaultPeriod.add(Calendar.DAY_OF_MONTH, Constants.DEFAULT_END_DATE_DAYS)

        val dateFormatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

        val startDate = dateFormatter.format(dateToday.time)
        val endDate = dateFormatter.format(dateAfterDefaultPeriod.time)

        return Pair(startDate, endDate)
    }

    fun deleteOldAsteroids() {
        viewModelScope.launch {
            database.deleteOldAsteroids()
        }
    }

    fun doneNavigating() {
        _navigateToAsteroid.value = null
    }

    fun navigateToAsteroid(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onHandledError() {
        _errorState.value = null
    }

}