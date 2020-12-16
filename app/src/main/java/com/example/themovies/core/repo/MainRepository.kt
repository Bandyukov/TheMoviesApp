package com.example.themovies.core.repo

import com.example.themovies.core.*
import com.example.themovies.core.database.dao.MovieDao
import com.example.themovies.core.mapping.toMovie
import com.example.themovies.core.mapping.toMovieDB
import com.example.themovies.core.models.Movie
import com.example.themovies.core.models.MovieVO
import java.lang.Exception

class MainRepository(
    private val internetSource: MoviesApiService,
    private val localSource: MovieDao
) {

    companion object {
        var flag = true
    }

    //========== Network ==========//
    suspend fun getAllMoviesFromNetwork(__page__: Int): List<Movie>? {
        try {
            // for test //
            //clearDatabase()
            val requestResponse = internetSource.getMoviesFromPage(__page__)

            val page = requestResponse.page
            val results = requestResponse.results
            if (flag) {

                clearDatabase()
                flag = false
            }

            updateDatabase(results)

            return results.map { it.toMovie() }
        } catch (e: Exception) {
            return null
        }
    }



    //========== Local ==========//
    suspend fun getAllMoviesFromDB(): List<Movie> {
        val resultResponse = localSource.getAllMovies()
        return resultResponse.map { it.toMovie() }
    }

    private suspend fun updateDatabase(moviesVO: List<MovieVO>) {
        localSource.insertListOfMovies(moviesVO.map { it.toMovieDB() })
    }

    suspend fun clearDatabase() {
        localSource.deleteAllMovies()
    }

}