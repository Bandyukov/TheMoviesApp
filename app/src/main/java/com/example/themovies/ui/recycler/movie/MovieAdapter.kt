package com.example.themovies.ui.recycler.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themovies.R
import com.example.themovies.core.models.movie.Movie
import com.squareup.picasso.Picasso

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = listOf<Movie>()

    companion object {
        private lateinit var onMovieClickListener: OnMovieClickListener

        fun setOnMovieClickListener(onMovieClickListener: OnMovieClickListener) {
            Companion.onMovieClickListener = onMovieClickListener
        }
    }

    private lateinit var onReachEndListener: OnReachEndListener

    fun setOnReachEndListener(onReachEndListener: OnReachEndListener) {
        this.onReachEndListener = onReachEndListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (/*movies.size >= 20 &&*/ movies.lastIndex - 4 == position && onReachEndListener != null) {
            onReachEndListener.onReachEnd()
        }
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun addListOfMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private var textViewReleaseDate: TextView = itemView.findViewById(R.id.textViewReleaseDate)
        private var imageViewPoster: ImageView = itemView.findViewById(R.id.imageViewPoster)

        init {
            itemView.setOnClickListener {
                onMovieClickListener.onClick(adapterPosition)
            }
        }

        fun bind(movie: Movie) {
            textViewTitle.text = movie.originalTitle
            textViewReleaseDate.text = movie.releaseDate
            val address = Movie.BASE_POSTER_URL + Movie.SMALL_POSTER_SIZE + movie.posterPath
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
}

