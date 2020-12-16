package com.example.themovies.core.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movie_table", )
data class MovieDB(

    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    @ColumnInfo(name = "poster_path")
    val posterPath: String,

    @ColumnInfo(name = "release_date")
    val releaseDate:String
) : Parcelable