package com.example.themovies.core.repo

import android.util.Log
import com.example.themovies.core.*
import com.example.themovies.core.database.dao.MovieDao
import com.example.themovies.core.database.entities.FavoriteMovieDB
import com.example.themovies.core.mapping.toMovie
import com.example.themovies.core.mapping.toMovieDB
import com.example.themovies.core.mapping.toReview
import com.example.themovies.core.mapping.toTrailer
import com.example.themovies.core.models.movie.Movie
import com.example.themovies.core.models.movie.MovieVO
import com.example.themovies.core.models.review.Review
import com.example.themovies.core.models.trailer.Trailer
import com.example.themovies.core.preferences.AppPreferences
import java.lang.Exception

class MainRepository(
    private val internetSource: MoviesApiService,
    private val localSource: MovieDao,
    private val appPreferences: AppPreferences
) {

    companion object {
        var flag = true
        var currentPage = 0
    }

    fun updatePage(page: Int) = appPreferences.setPage(page)

    fun updateSort(sort: Int) = appPreferences.setSort(sort)

    fun getPage() : Int = appPreferences.getPage()

    fun getSort() : Int = appPreferences.getSort()


    //========== Network ==========//
    suspend fun getAllMoviesFromNetwork(methodOfSort: String, __page__: Int) {
        try {
            //clearDatabase()

            val requestResponse = internetSource.getMoviesFromNet(
                methodOfSort,
                includeAdult = false,
                includeVideo = true,
                __page__,
                800
            )

            currentPage = requestResponse.page
            val results = requestResponse.results

            if (flag) {
                clearDatabase()
                flag = false
            }

            updateDatabase(results)
        } catch (ex: Exception) {
            return
        }
    }

    suspend fun getTrailers(movieId: Int): List<Trailer>? {
        return try {
            val request = internetSource.getTrailers(movieId)

            val results = request.results

            results.map { it.toTrailer() }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getReviews(movieId: Int, page: Int): List<Review>? {
        return try {
            val request = internetSource.getReviews(movieId, page)

            val results = request.result

            results.map { it.toReview() }
        } catch (e: Exception) {
            null
        }
    }


    //========== Local ==========//
    suspend fun getAllMoviesFromDB(): List<Movie> {
        val resultResponse = localSource.getAllMovies()
        return resultResponse.map { it.toMovie() }
    }

    private suspend fun updateDatabase(moviesVO: List<MovieVO>) =
        localSource.insertListOfMovies(moviesVO.map { it.toMovieDB() })

    suspend fun clearDatabase() = localSource.deleteAllMovies()

    suspend fun getMovie(id: Int): Movie = localSource.getMovieById(id).toMovie()



    suspend fun getAllFavoriteMoviesFromDB(): List<FavoriteMovieDB> =
        localSource.getAllFavoriteMovies()

    suspend fun insertFavoriteMovie(favoriteMovie: FavoriteMovieDB) =
        localSource.insertFavoriteMovie(favoriteMovie)

    suspend fun getFavoriteMovie(id: Int) : Movie? {
        val movieFavDB = localSource.getFavoriteMovieById(id) ?: return null
        return movieFavDB.toMovie()
    }

    suspend fun deleteFavoriteMovieFromDB(favoriteMovie: FavoriteMovieDB) =
        localSource.deleteFavoriteMovie(favoriteMovie)

    suspend fun deleteAllFavoriteMovies() = localSource.deleteAllFavoriteMovies()


}