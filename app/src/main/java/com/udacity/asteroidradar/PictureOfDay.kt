package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

//Picture of the day entity and object
@Entity
data class PictureOfDay(@PrimaryKey @Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)



