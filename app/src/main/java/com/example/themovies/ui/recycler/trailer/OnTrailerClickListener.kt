package com.example.themovies.ui.recycler.trailer

interface OnTrailerClickListener {
    fun onShortClick(position: Int)
    fun onLongClick(position: Int)
}