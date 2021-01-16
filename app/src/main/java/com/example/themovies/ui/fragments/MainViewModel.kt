package com.example.themovies.ui.fragments

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovies.R
import com.example.themovies.core.mapping.toFavoriteMovieDB
import com.example.themovies.core.mapping.toMovie
import com.example.themovies.core.models.movie.Movie
import com.example.themovies.core.models.review.Review
import com.example.themovies.core.models.trailer.Trailer
import com.example.themovies.core.repo.MainRepository
import kotlinx.coroutines.*
import java.lang.NullPointerException

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    private val job: Job
    private val uiScope: CoroutineScope

    private val _listOfMovies_: MutableLiveData<List<Movie>>
    val listOfMovies: LiveData<List<Movie>>
        get() = _listOfMovies_

    private val _listOfFavoriteMovies_: MutableLiveData<List<Movie>>
    val listOfFavoriteMovies: LiveData<List<Movie>>
        get() = _listOfFavoriteMovies_

    private val _listOfTrailers_ = MutableLiveData<List<Trailer>>()
    val listOfTrailers: LiveData<List<Trailer>> get() = _listOfTrailers_

    private val _listOfReviews_ = MutableLiveData<List<Review>>()
    val listOfReview: LiveData<List<Review>> get() = _listOfReviews_


    private val _movie_ = MutableLiveData<Movie>()
    val movie: LiveData<Movie> get() = _movie_

    private val _movieFav_ = MutableLiveData<Movie?>()
    val movieFav: LiveData<Movie?> get() = _movieFav_


    private val _isLoadingGoing_ = MutableLiveData<Boolean>()
    val isLoadingGoing: LiveData<Boolean> get() = _isLoadingGoing_

    private val _page_ = MutableLiveData<Int>()
    val page: LiveData<Int> get() = _page_

    private val _sort_ = MutableLiveData<Int>()
    val sort: LiveData<Int> get() = _sort_

    companion object {
        var flag = false

        private const val REVENUE_PARAM = "revenue.desc"
        private const val POPULARITY_PARAM = "popularity.desc"
        private const val RELEASE_PARAM = "release_date.desc"
        private const val VOTE_AVERAGE_PARAM = "vote_average.desc"
        private const val VOTE_COUNT_PARAM = "vote_count.desc"
    }


    init {
        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)
        _page_.value = repository.getPage()
        _sort_.value = repository.getSort()
        _listOfMovies_ = MutableLiveData<List<Movie>>()
        _listOfFavoriteMovies_ = MutableLiveData<List<Movie>>()
        _isLoadingGoing_.value = true

    }

    fun increasePage() {
        val next = _page_.value!!.plus(1)
        _page_.value = next
        repository.updatePage(next)
    }

    fun setDefaultPage() {
        _page_.value = 1
        repository.updatePage(1)
    }

    fun setMethodOfSort(sort: Int) {
        _sort_.value = sort
        repository.updateSort(sort)
    }

    fun setDefaultValues() {
        setDefaultPage()
        MainRepository.flag = true
    }

    fun getMovie(id: Int) {
        uiScope.launch {
            getFavoriteMovie(id)

            try {
                _movie_.value = repository.getMovie(id)
            } catch (e: NullPointerException) {
                _movie_.value = repository.getFavoriteMovie(id)
            }
        }
    }

    fun getAllMovies(methodOfSort: Int) {
        uiScope.launch {
            _page_.value?.let {
                val currentSort = when(methodOfSort) {
                    0 -> POPULARITY_PARAM
                    1 -> VOTE_AVERAGE_PARAM
                    2 -> REVENUE_PARAM
                    else -> VOTE_COUNT_PARAM
                }
                repository.getAllMoviesFromNetwork(currentSort, it)
            }

            _isLoadingGoing_.value = true
            val result = repository.getAllMoviesFromDB()
            _listOfMovies_.value = result
            _isLoadingGoing_.value = false
        }

    }

    fun getAllMoviesFromDatabase() {
        uiScope.launch {
            val result = repository.getAllMoviesFromDB()
            _listOfMovies_.value = result
        }
    }

    fun deleteAllMoviesFromDatabase() {
        uiScope.launch {
            repository.clearDatabase()
        }
    }

    fun getTrailers() {
        uiScope.launch {
            _movie_.value?.let { movie ->
                val response = repository.getTrailers(movie.id)
                val trailers = mutableListOf<Trailer>()
                response?.let {
                    for (now in it)
                        if (now.type == "Trailer")
                            trailers.add(now)

                    _listOfTrailers_.value = trailers
                }
            }
        }
    }

    fun getReviews() {
        uiScope.launch {
            _movie_.value?.let { movie ->
                val response = repository.getReviews(movie.id, 1)

                response?.let {
                    _listOfReviews_.value = it
                }
            }
        }
    }

    fun addToFavorite() {
        uiScope.launch {
            _movie_.value?.let {
                getFavoriteMovie(it.id)
                repository.insertFavoriteMovie(it.toFavoriteMovieDB())
            }
        }
    }

    private fun deleteFavoriteMovie(movie: Movie) {
        uiScope.launch {
            repository.deleteFavoriteMovieFromDB(movie.toFavoriteMovieDB())
            getAllMoviesFromFavoriteDB()
        }
    }

    fun getAllMoviesFromFavoriteDB() {
        uiScope.launch {
            val result = repository.getAllFavoriteMoviesFromDB()
            _listOfFavoriteMovies_.value = result.map { it.toMovie() }
        }
    }

    private fun getFavoriteMovie(id: Int) {
        uiScope.launch {
            _movieFav_.value = repository.getFavoriteMovie(id)
        }
    }

    fun clearFavoriteDB() {
        uiScope.launch {
            repository.deleteAllFavoriteMovies()
        }
    }

    fun addOrDelete(position: Int, isFavorite: Boolean, context: Context) {
        uiScope.launch {
            val id = if (isFavorite)
                _listOfFavoriteMovies_.value!![position].id
            else
                _listOfMovies_.value!![position].id

            val movieFromFavDB = repository.getFavoriteMovie(id)

            val message: String = if (movieFromFavDB == null) {
                addToFavorite()
                context.getString(R.string.was_added)
            } else {
                deleteFavoriteMovie(movieFromFavDB)
                context.getString(R.string.was_deleted)
            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}