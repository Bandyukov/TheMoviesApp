package com.example.themovies.core.mapping

import android.util.Log
import com.example.themovies.core.Movie
import com.example.themovies.core.MovieVO
import com.example.themovies.core.database.entities.MovieDB

private const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/"
private const val SMALL_POSTER_SIZE = "w185"

fun MovieVO.toMovie(): Movie {
    return Movie(id, originalTitle, posterPath, releaseDate)
}

fun MovieDB.toMovie(): Movie {
    return Movie(id, originalTitle, posterPath, releaseDate)
}

fun Movie.toMovieDB(): MovieDB {
    return MovieDB(id, originalTitle, posterPath, releaseDate)
}

fun MovieVO.toMovieDB(): MovieDB {
    return MovieDB(id, originalTitle, posterPath, releaseDate)
}
