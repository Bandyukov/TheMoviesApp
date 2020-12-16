package com.example.themovies.ui.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themovies.R
import com.example.themovies.core.Movie
import com.squareup.picasso.Picasso
import java.lang.Exception

private const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/"
private const val SMALL_POSTER_SIZE = "w185"


class MovieAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    private val movies = mutableListOf<Movie>()

    private lateinit var onReachEndListener: OnReachEndListener

    fun setOnReachEndListener(onReachEndListener: OnReachEndListener) {
        this.onReachEndListener = onReachEndListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (movies.size >= 20 && movies.lastIndex == position && onReachEndListener != null) {
            onReachEndListener.onReachEnd()
        }
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun addListOfMovies(newMovies: List<Movie>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
    var textViewReleaseDate: TextView = itemView.findViewById(R.id.textViewReleaseDate)
    var imageViewPoster: ImageView = itemView.findViewById(R.id.imageViewPoster)

    fun bind(movie: Movie) {
        textViewTitle.text = movie.originalTitle
        textViewReleaseDate.text = movie.releaseDate
        val address = BASE_POSTER_URL + SMALL_POSTER_SIZE + movie.posterPath
        Picasso.get().load(address).into(imageViewPoster)
        Picasso.get().load(address).error(android.R.drawable.ic_dialog_alert).into(imageViewPoster)
    }

    companion object {
        fun from(parent: ViewGroup): MovieViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
            return MovieViewHolder(view)
        }

    }
}