package com.example.themovies.core.database.dao

import androidx.room.*
import com.example.themovies.core.database.entities.FavoriteMovieDB
import com.example.themovies.core.database.entities.MovieDB

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie_table")
    suspend fun getAllMovies() : List<MovieDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieDB: MovieDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfMovies(listOfMovies: List<MovieDB>)

    @Delete
    suspend fun deleteMovie(movieDB: MovieDB)

    @Query("SELECT * FROM movie_table WHERE uniqueId ==:movieId")
    suspend fun getMovieById(movieId: Int) : MovieDB

    @Query("DELETE FROM movie_table")
    suspend fun deleteAllMovies()


    @Query("SELECT * FROM favorite_movies_table")
    suspend fun getAllFavoriteMovies() : List<FavoriteMovieDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(favoriteMovie: FavoriteMovieDB)

    @Query("SELECT * FROM favorite_movies_table WHERE uniqueId ==:movieId")
    suspend fun getFavoriteMovieById(movieId: Int) : FavoriteMovieDB

    @Delete
    suspend fun deleteFavoriteMovie(favoriteMovie: FavoriteMovieDB)

}