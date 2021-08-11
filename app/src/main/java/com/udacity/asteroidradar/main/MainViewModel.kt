package com.udacity.asteroidradar.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Repository
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE


class MainViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = Repository(getDatabase(application))

    //Live data values from repository
    val weekAsteroidList = repository.asteroidsWeek
    val asteroidListToday = repository.asteroidsToday
    val todaysPicture = repository.pictureOfDay
    val savedAsteroidsList = repository.asteroidsFavourites

    //Live data for the current list of displayed asteroids for recycler view
    val displayAsteroids = MediatorLiveData<List<Asteroid>>()


    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError


    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    //Asteroid to pass to details fragment
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    //Boolean for if any saved asteroids exist
    private var _existFavourites :Boolean = false
    val existFavourites: Boolean
        get() = _existFavourites


    //refresh data and show week asteroid list on init
    init {
        refreshDataFromRepository()
        showWeekAsteroids()
    }

    //function using coroutine to refresh the repository, connecting to NASA API
    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                repository.refreshAsteroids()
                repository.refreshPictureOfDay()
                _existFavourites = repository.checkFavourites()

                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(asteroidListToday.value.isNullOrEmpty())
                   _eventNetworkError.value = true
            }
        }
    }

    //functions for navigation to details screen
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    //functions to change boolean for if there are saved asteroids
    fun setFavouritesFound(){
        _existFavourites = true
    }

    fun resetFavouritesFound(){
        _existFavourites = false
    }

    //functions to change the displayed asteroid list using the mediator live data
    fun showTodayAsteroids(){
        replaceDisplayAsteroids(asteroidListToday)
    }

    fun showWeekAsteroids(){

        replaceDisplayAsteroids(weekAsteroidList)
    }

    fun showSavedAsteroids(){
       if (_existFavourites) replaceDisplayAsteroids(savedAsteroidsList)
    }

    //Updates mediator live data with correct list to show
    fun replaceDisplayAsteroids(showAsteroids:LiveData<List<Asteroid>>){
        displayAsteroids.removeSource(asteroidListToday)
        displayAsteroids.removeSource(weekAsteroidList)
        displayAsteroids.removeSource(savedAsteroidsList)
        displayAsteroids.addSource(showAsteroids){displayAsteroids.value = it}
    }

}