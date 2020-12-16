package com.example.themovies.core.repo

import android.util.Log
import com.example.themovies.core.*
import com.example.themovies.core.database.dao.MovieDao
import com.example.themovies.core.mapping.toMovie
import com.example.themovies.core.mapping.toMovieDB
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
            Log.i("res", "try to get data from net page = $__page__")
            val requestResponse = internetSource.getMoviesFromPage(__page__)
            Log.i("res", "managed to ")
            val page = requestResponse.page
            val results = requestResponse.results
            if (flag) {
                Log.i("res", "movies should be received from server\n database will be cleared")
                clearDatabase()
                flag = false
            }
            Log.i("res", "DATABASE UPDATED")
            updateDatabase(results)
            if (results.isNullOrEmpty())
                Log.i("res", "Null from net")
            return results.map { it.toMovie() }
        } catch (e: Exception) {
            Log.i("res", "we inside catch")
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
        Log.i("res", "====DATABASE WAS UPDATED!!!====")
    }

    suspend fun clearDatabase() {
        localSource.deleteAllMovies()
    }

}