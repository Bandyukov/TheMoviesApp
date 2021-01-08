package com.example.themovies.core.models.review

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewVO(
    @Json(name = "author")
    val author: String,

    @Json(name = "content")
    val content: String,

    @Json(name = "created_at")
    val postingDate: String
) : Parcelable