package com.example.themovies.core.mapping

import com.example.themovies.core.models.Movie
import com.example.themovies.core.models.MovieVO
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
