package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Repository(private val database: AsteroidDatabase) {

    //Retrieve asteroid lists from database and convert to domain model live data
    val asteroidsWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.AsteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    val asteroidsToday: LiveData<List<Asteroid>> =
        Transformations.map(database.AsteroidDao.getAsteroidsToday(Constants.getDate())) {
            it.asDomainModel()
        }

    val asteroidsFavourites: LiveData<List<Asteroid>> =
        Transformations.map(database.AsteroidDao.getFavouriteAsteroids()) {
            it.asDomainModel()
        }

    //retrieve picture of the day live data from database
    val pictureOfDay: LiveData<PictureOfDay> = database.pictureOfDayDao.getPicOfDay()

    //coroutine to set asteroid as saved asteroid in dataabse
    suspend fun setFavourite(id: Long){
        withContext(Dispatchers.IO) {
            database.AsteroidDao.addFav(id)
        }
    }

    //coroutine to check for any saved asteroids in database
    suspend fun checkFavourites(): Boolean {
        var check: Boolean = false
        withContext(Dispatchers.IO) {
            check = database.AsteroidDao.checkFavouriteAsteroids()
        }
        return check
    }

    //coroutine to delete old asteroids from database
    suspend fun deletOldAsteroids(){
        withContext(Dispatchers.IO) {

            database.AsteroidDao.deleteOldAsteroids(Constants.getDate())
        }
    }

    //coroutine function to get asteroid data via API, parse and convert to database model, then insert to database
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {

            val astResp = AsteroidApi.retrofitService.getAsteroids(Constants.API_KEY)
            val asteroidsList = parseAsteroidsJsonResult(JSONObject(astResp))
            database.AsteroidDao.insertAll(asteroidsList)
        }
    }

    //coroutine function to get pic of day via API and add to database
    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            val pic = AsteroidApi.retrofitService.getPictureOfDay(Constants.API_KEY)
            database.pictureOfDayDao.insertAll(pic)
        }

    }

}
