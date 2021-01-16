package com.example.themovies.core.database.database_itself

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.themovies.core.database.entities.MovieDB
import com.example.themovies.core.database.dao.MovieDao
import com.example.themovies.core.database.entities.FavoriteMovieDB

@Database(entities = [MovieDB::class, FavoriteMovieDB::class], version = 10, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao

    companion object {
        private const val DATABASE_NAME = "movie.db"

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context) : MovieDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context, MovieDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}