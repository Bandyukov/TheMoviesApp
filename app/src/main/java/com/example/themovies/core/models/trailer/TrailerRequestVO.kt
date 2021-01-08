package com.example.themovies.core.models.trailer

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrailerRequestVO(
    @Json(name = "id")
    var id: Int,

    @Json(name = "results")
    var results: List<TrailerVO>
) : Parcelable