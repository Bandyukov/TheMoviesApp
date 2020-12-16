package com.example.themovies.core

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestResponseVO(

    @Json(name = "page")
    var page: Int,

    @Json(name = "results")
    var results: List<MovieVO>

) : Parcelable