package com.example.themovies.core.models.review

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewsRequestVO(
    @Json(name = "results")
    val result: List<ReviewVO>
) : Parcelable