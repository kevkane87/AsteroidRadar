package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Repository
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }


    //Background work to refresh data every day and delete old asteroids from the database
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = Repository(database)
        return try {
            repository.refreshAsteroids()
            repository.refreshPictureOfDay()
            repository.deletOldAsteroids()
            Result.success()
        } catch (e: HttpException) {
            repository.deletOldAsteroids()
            Result.retry()
        }
    }
}
