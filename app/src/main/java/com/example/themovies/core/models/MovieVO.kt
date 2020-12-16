package com.example.themovies.core.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieVO(

    @Json(name = "id")
    val id: Int,

    @Json(name = "original_title")
    val originalTitle: String,

    @Json(name = "poster_path")
    val posterPath: String,

    @Json(name = "release_date")
    val releaseDate:String
) : Parcelable