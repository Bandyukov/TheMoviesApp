package com.example.themovies.core.database.dao

import androidx.room.*
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

    @Query("SELECT * FROM movie_table WHERE id==:movieId")
    suspend fun getMovieById(movieId: Int) : MovieDB

    @Query("DELETE FROM movie_table")
    suspend fun deleteAllMovies()

}