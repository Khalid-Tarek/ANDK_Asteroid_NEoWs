package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.NASANEoWsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDao
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    val database: AsteroidDao,
    application: Application
) : AndroidViewModel(application) {

    private val TAG = "MainViewModel"

    //private val _asteroids = database.getAllAsteroids()
    private val _asteroids = MutableLiveData<MutableList<Asteroid>>()
    val asteroids: LiveData<MutableList<Asteroid>>
        get() = _asteroids

    init {
        getAsteroids()
    }

    private fun getAsteroids() {
        NASANEoWsApi.retrofitService.getAsteroids().enqueue( object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i(TAG, "Failure: $t")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i(TAG, response.toString())
                val newList = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                Log.i(TAG, newList.toString())
                _asteroids.value = newList
            }
        })

    }

    private fun testSomeAsteroids() {
        _asteroids.value = mutableListOf(
            Asteroid(50,
            "Test Codename",
            "2020-20-02",
            20.0,
            20.0,
            20.0,
            20.0,
            false),
            Asteroid(70,
                "Alpha 20SX12",
                "2022-01-01",
                10.0,
                10.0,
                25.0,
                26.0,
                true))
    }

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