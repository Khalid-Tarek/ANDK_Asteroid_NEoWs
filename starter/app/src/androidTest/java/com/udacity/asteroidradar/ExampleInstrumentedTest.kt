package com.udacity.asteroidradar

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var asteroidDao: AsteroidDao
    private lateinit var db: AsteroidDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AsteroidDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        asteroidDao = db.asteriodDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun deleteOldAsteroid() {
        runBlocking {
            val asteroid = Asteroid(1,
                "",
                "2022-07-07",
            0.0,
            0.0,
            0.0,
            0.0,
            false)
            asteroidDao.insert(asteroid)

            var currentAsteroid = asteroidDao.get(1)
            assertEquals(currentAsteroid, asteroid)

            asteroidDao.deleteAsteroidsBefore("2022-08-08")
            currentAsteroid = asteroidDao.get(1)
            assertNull(currentAsteroid)
        }
    }
}
