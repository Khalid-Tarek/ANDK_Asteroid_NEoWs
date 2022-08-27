package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Insert
    suspend fun insert(asteroid: Asteroid)

    @Update
    suspend fun update(asteroid: Asteroid)

    @Delete
    suspend fun delete(asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_table WHERE id = :value")
    suspend fun get(value: Long): Asteroid?

    @Query("DELETE FROM asteroid_table")
    suspend fun clear()

    @Query("SELECT * FROM asteroid_table ORDER BY id DESC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    //TODO: Delete all asteroids before today
    //@Query("DELETE FROM asteroid_table WHERE ")
    suspend fun deleteOldAsteroids(){

    }
}