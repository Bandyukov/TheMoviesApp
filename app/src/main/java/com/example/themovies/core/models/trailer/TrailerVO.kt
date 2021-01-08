package com.example.themovies.core.models.trailer

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrailerVO (
    @Json(name = "key")
    val key: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "type")
    val type: String
) : Parcelable
