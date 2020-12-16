package com.example.themovies.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovies.core.Connection
import com.example.themovies.core.Movie
import com.example.themovies.core.repo.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    private val job: Job
    private val uiScope: CoroutineScope

    private val _listOfMovies_: MutableLiveData<MutableList<Movie>>
    val listOfMovies: LiveData<MutableList<Movie>>
        get() = _listOfMovies_


    private val _isLoadingGoing_ = MutableLiveData<Boolean>()
    val isLoadingGoing: LiveData<Boolean> get() = _isLoadingGoing_

    private val _page_ = MutableLiveData<Int>()
    val page : LiveData<Int> get() = _page_

    init {
        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)
        _page_.value = 1
        _listOfMovies_ = MutableLiveData<MutableList<Movie>>()
        _isLoadingGoing_.value = true
    }

    fun increasePage() {
        _page_.value = _page_.value!!.plus(1)
    }

    fun setDefaultPage() {
        _page_.value = 1
    }

    fun getAllMovies() {
        uiScope.launch {
            try {
                _isLoadingGoing_.value = true
                val result = repository.getAllMoviesFromNetwork(_page_.value!!)
                if (!result.isNullOrEmpty()) {
                    updateList(result)
                }
                else {
                    getAllMoviesFromDatabase()
                    Connection.isInternetConnection = false
                }
            } catch (e: Exception) {
            } finally {
                _isLoadingGoing_.value = false
            }

        }

    }


    fun getAllMoviesFromNetwork() {
        uiScope.launch {
            try {
                _isLoadingGoing_.value = true
                val result = repository.getAllMoviesFromNetwork(_page_.value!!)
                if (!result.isNullOrEmpty()) {
                    updateList(result)
                }
            } catch (e: Exception) {
                Log.i("res", "error in getting data???")
            } finally {
                _isLoadingGoing_.value = false
            }
        }
    }

    fun updateList(result: List<Movie>) {
        val listRequired = _listOfMovies_.value
        if (listRequired == null) {
            _listOfMovies_.value = result.toMutableList()
        } else {
            listRequired.clear()
            listRequired.addAll(result)

            _listOfMovies_.value = listRequired.toMutableList()
        }
    }

    fun getAllMoviesFromDatabase() {
        uiScope.launch {
            val result = repository.getAllMoviesFromDB()
            _listOfMovies_.value = result.toMutableList()
        }
    }

    fun deleteAllMoviesFromDatabase() {
        uiScope.launch {
            repository.clearDatabase()
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}