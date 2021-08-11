package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Repository
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(AsteroidDatabase.getDatabase(application))

    //coroutine function to save asteroid by setting it as favourite in database
    fun setFavToDatabase(id: Long){
        viewModelScope.launch {
            try {
                repository.setFavourite(id)
            }
            catch (networkError: IOException){
            }
        }
    }
}