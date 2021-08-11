package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.PictureOfDay


//Connect with picture of day database using Room
@Dao
interface PictureOfDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pic: PictureOfDay)

    @Query("SELECT * from pictureofday")
    fun getPicOfDay(): LiveData<PictureOfDay>

}
