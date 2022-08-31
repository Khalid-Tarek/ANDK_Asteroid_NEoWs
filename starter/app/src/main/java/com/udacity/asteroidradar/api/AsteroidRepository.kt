package com.udacity.asteroidradar.api

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    enum class AsteroidApiFilter { SHOW_ALL, SHOW_WEEK, SHOW_TODAY }

    val asteroids: LiveData<List<Asteroid>> = database.asteriodDao.getAllAsteroids()

    suspend fun refreshAsteroids() = withContext(Dispatchers.IO) {
        val (startDate, endDate) = getQueryDates()
        val response = NASANEoWsApi.retrofitService.getAsteroids(startDate, endDate).await()
        val asteroids = parseAsteroidsJsonResult(JSONObject(response)) as List<Asteroid>
        database.asteriodDao.insertAll(*asteroids.asDatabaseModel())
    }

    suspend fun deleteOldAsteroids() = withContext(Dispatchers.IO) {
        val dateToday = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            .format(Calendar.getInstance().time)
        database.asteriodDao.deleteAsteroidsBefore(dateToday)
    }

}

fun getQueryDates(): Pair<String, String> {
    val dateToday = Calendar.getInstance()
    val dateAfterDefaultPeriod = dateToday.clone() as Calendar
    dateAfterDefaultPeriod.add(Calendar.DAY_OF_MONTH, Constants.DEFAULT_END_DATE_DAYS)

    val dateFormatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    val startDate = dateFormatter.format(dateToday.time)
    val endDate = dateFormatter.format(dateAfterDefaultPeriod.time)

    return Pair(startDate, endDate)
}