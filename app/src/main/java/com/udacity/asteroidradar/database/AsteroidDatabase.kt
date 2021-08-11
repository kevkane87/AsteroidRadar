package com.udacity.asteroidradar.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.PictureOfDay

//Creates database using Room
@Database(entities = [DatabaseAsteroid::class , PictureOfDay::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract val AsteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao

    companion object {

        @Volatile
        private var INSTANCE: AsteroidDatabase? = null

        fun getDatabase(context: Context): AsteroidDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroids"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
