package com.example.themovies.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovies.core.models.movie.Movie
import com.example.themovies.core.models.review.Review
import com.example.themovies.core.models.trailer.Trailer
import com.example.themovies.core.repo.MainRepository
import kotlinx.coroutines.*

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    private val job: Job
    private val uiScope: CoroutineScope

    private val _listOfMovies_: MutableLiveData<List<Movie>>
    val listOfMovies: LiveData<List<Movie>>
        get() = _listOfMovies_

    private val _listOfTrailers_ = MutableLiveData<List<Trailer>>()
    val listOfTrailers : LiveData<List<Trailer>> get() = _listOfTrailers_

    private val _listOfReviews_ = MutableLiveData<List<Review>>()
    val listOfReview: LiveData<List<Review>> get() = _listOfReviews_


    private val _movie_ = MutableLiveData<Movie>()
    val movie : LiveData<Movie> get() = _movie_


    private val _isLoadingGoing_ = MutableLiveData<Boolean>()
    val isLoadingGoing: LiveData<Boolean> get() = _isLoadingGoing_

    private val _page_ = MutableLiveData<Int>()
    val page : LiveData<Int> get() = _page_

    companion object {
        var flag = false
    }

    init {
        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)
        _page_.value = 1
        _listOfMovies_ = MutableLiveData<List<Movie>>()
        _isLoadingGoing_.value = true

    }

    fun increasePage() {
        _page_.value = _page_.value?.plus(1)
        //_page_.value = (_listOfMovies_.value!!.size / 20) + 1
    }

    fun setDefaultPage() {
        _page_.value = 1
    }

    fun getMovie(id: Int) {
        uiScope.launch {
            _movie_.value = repository.getMovie(id)
            Log.i("cv", "succeded to get movie by id")
        }
    }

    fun getAllMovies() {
        uiScope.launch {
//            try {
//                _isLoadingGoing_.value = true
//                val result = repository.getAllMoviesFromNetwork(_page_.value!!)
//                if (!result.isNullOrEmpty()) {
//                    updateList(result)
//                }
//                else {
//                    getAllMoviesFromDatabase()
//                    Connection.isInternetConnection = false
//                }
//            } catch (e: Exception) {
//            } finally {
//                _isLoadingGoing_.value = false
//            }
            _page_.value?.let {
                repository.getAllMoviesFromNetwork(it)
            }

            _isLoadingGoing_.value = true
            val result = repository.getAllMoviesFromDB()
            updateList(result)
            _isLoadingGoing_.value = false
        }

    }


//    fun getAllMoviesFromNetwork() {
//        uiScope.launch {
//            try {
//                _isLoadingGoing_.value = true
//                val result = repository.getAllMoviesFromNetwork(_page_.value!!)
//                if (!result.isNullOrEmpty()) {
//                    updateList(result)
//                }
//            } catch (e: Exception) {
//                Log.i("res", "error in getting data???")
//            } finally {
//                _isLoadingGoing_.value = false
//            }
//        }
//    }

    private fun updateList(result: List<Movie>) {
//        val listRequired = _listOfMovies_.value
//        if (listRequired == null) {
//            _listOfMovies_.value = result.toMutableList()
//        } else {
//            listRequired.clear()
//            listRequired.addAll(result)
//
//            _listOfMovies_.value = listRequired.toMutableList()
//        }
        _listOfMovies_.value = result
        Log.i("page", "list size is ${_listOfMovies_.value!!.size}")
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
                        if(now.type == "Trailer")
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}