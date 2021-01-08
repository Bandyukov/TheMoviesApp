package com.example.themovies.core.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorite_movies_table")
data class FavoriteMovieDB(
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "rating")
    val rating: Double,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int,

    ) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uniqueId: Int = 0

    constructor(
        id: Int,
        originalTitle: String,
        overview: String,
        posterPath: String?,
        releaseDate: String,
        rating:Double,
        voteCount: Int,
        uniqueId: Int = 0
    )
            : this(id, originalTitle, overview, posterPath, releaseDate, rating, voteCount) {
        this.uniqueId = uniqueId
    }
}