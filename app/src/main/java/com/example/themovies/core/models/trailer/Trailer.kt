package com.example.themovies.core.models.trailer


data class Trailer (
    val key: String,

    val name: String,

    val type: String

    ) {

    companion object {
        const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="
    }
}

