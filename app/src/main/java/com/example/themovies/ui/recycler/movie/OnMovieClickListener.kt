package com.example.themovies.ui.recycler.movie

interface OnMovieClickListener {
    fun onClick(position: Int)
    fun onLongClick(position: Int)
}