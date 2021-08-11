package com.udacity.asteroidradar.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class DatabaseAsteroid constructor (@PrimaryKey val id: Long, val codename: String, val closeApproachDate: String,
                                         val absoluteMagnitude: Double, val estimatedDiameter: Double,
                                         val relativeVelocity: Double, val distanceFromEarth: Double,
                                         val isPotentiallyHazardous: Boolean, val isFavourite: Boolean) : Parcelable


//mapping function
fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous,
            isFavourite = it.isFavourite
        )
    }
}






