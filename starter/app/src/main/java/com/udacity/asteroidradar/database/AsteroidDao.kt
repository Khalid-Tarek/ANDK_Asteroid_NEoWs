package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: Asteroid)

    @Update
    suspend fun update(asteroid: Asteroid)

    @Delete
    suspend fun delete(asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_table WHERE id = :value")
    suspend fun get(value: Long): Asteroid?

    @Query("DELETE FROM asteroid_table")
    suspend fun clear()

    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroid_table WHERE :date > closeApproachDate")
    suspend fun deleteAsteroidsBefore(date: String)
}