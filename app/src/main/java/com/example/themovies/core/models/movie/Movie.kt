package com.example.themovies.core.models.movie

import androidx.room.ColumnInfo


data class Movie(
    val id: Int,

    val originalTitle: String,

    val overview: String,

    val posterPath: String?,

    val releaseDate:String,

    val rating: Double,

    val voteCount: Int,
) {
    var uniqueId: Int = 0

    constructor(
        id: Int,
        originalTitle: String,
        overview: String,
        posterPath: String?,
        releaseDate: String,
        rating:Double,
        voteCount: Int,
        uniqueId: Int = 0
    )
            : this(id, originalTitle, overview, posterPath, releaseDate, rating, voteCount) {
        this.uniqueId = uniqueId
    }

    companion object {
        const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/"
        const val SMALL_POSTER_SIZE = "w185"
        const val BIG_POSTER_SIZE = "w780"
    }
}