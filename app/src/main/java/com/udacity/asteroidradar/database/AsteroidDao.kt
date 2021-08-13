package com.udacity.asteroidradar.database
import androidx.lifecycle.LiveData
import androidx.room.*
import retrofit2.http.DELETE
import java.util.*
import kotlin.collections.ArrayList

//Connect with asteroid database using Room
@Dao
interface AsteroidDao {

    //insert new asteroids to database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(asteroids: ArrayList<DatabaseAsteroid>)

    //set asteroid as favourite in database
    @Query("UPDATE databaseasteroid SET isFavourite = 1 WHERE id = :id ")
    fun addFav(id: Long)

    //retrieve list of asteroids from database as live data
    @Query("SELECT * from databaseasteroid ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    //retrieve list of today's asteroids from database as live data
    @Query("SELECT * from databaseasteroid WHERE closeApproachDate =:date  ORDER BY closeApproachDate ASC")
    fun getAsteroidsToday(date: String): LiveData<List<DatabaseAsteroid>>

    //retrieve list of favourite asteroids from database as live data
    @Query("SELECT * from databaseasteroid WHERE isFavourite = 1 ORDER BY closeApproachDate ASC")
    fun getFavouriteAsteroids(): LiveData<List<DatabaseAsteroid>>

    //check for favourites in database
    @Query("SELECT EXISTS (SELECT 1 FROM databaseasteroid WHERE isFavourite = 1 ORDER BY closeApproachDate ASC)")
    fun checkFavouriteAsteroids(): Boolean

    //delete asteroids before today from database
    @Query("DELETE from databaseasteroid WHERE closeApproachDate < :date")
    fun deleteOldAsteroids(date: String)

}