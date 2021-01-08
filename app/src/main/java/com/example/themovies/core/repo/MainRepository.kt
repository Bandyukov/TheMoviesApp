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
import java.lang.Exception

class MainRepository(
    private val internetSource: MoviesApiService,
    private val localSource: MovieDao
) {

    companion object {
        var flag = true
        var currentPage = 0
        private const val REVENUE_PARAM = "revenue.desc"
        private const val POPULARITY_PARAM = "popularity.desc"
        private const val RELEASE_PARAM = "release_date.desc"
        private const val VOTE_AVERAGE_PARAM = "vote_average.desc"
        private const val VOTE_COUNT_PARAM = "vote_count.desc"
    }

    //========== Network ==========//
    suspend fun getAllMoviesFromNetwork(__page__: Int) {
        try {
            //clearDatabase()
            //val requestResponse = internetSource.getMoviesFromPage(__page__)//
                Log.i("page", "Try to get from net")

            val requestResponse = internetSource.getMoviesFromNet(
                VOTE_COUNT_PARAM,
                includeAdult = false,
                includeVideo = true,
                __page__,
                800
            )
            Log.i("page", "Managed to get from net")

            currentPage = requestResponse.page
            Log.i("page", "page from net = $currentPage")
            val results = requestResponse.results

            if (flag) {
                Log.i("page", "DB CLEARED")
                clearDatabase()
                flag = false
            }

            updateDatabase(results)
        } catch (ex: Exception) {
            Log.i("page", "Unsuccessful")
            return
        }
    }

    suspend fun getTrailers(movieId: Int): List<Trailer>? {
        return try {
            val request = internetSource.getTrailers(movieId)

            val id = request.id
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

    suspend fun deleteFavoriteMovieFromDB(favoriteMovie: FavoriteMovieDB) =
        localSource.deleteFavoriteMovie(favoriteMovie)


}