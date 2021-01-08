package com.example.themovies.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.themovies.R
import com.example.themovies.core.MoviesApi
import com.example.themovies.core.database.database_itself.MovieDatabase
import com.example.themovies.core.repo.MainRepository
import com.example.themovies.databinding.FragmentFavoriteMoviesBinding

class FavoriteMoviesFragment : Fragment() {

    private val mainViewModel by lazy {
        val database = MovieDatabase.getInstance(requireContext().applicationContext)
        val repository = MainRepository(MoviesApi.moviesApiService, database.movieDao)
        val viewModelFactory = MainViewModelFactory(repository)
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFavoriteMoviesBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_favorite_movies,
            container,
            false
        )



        return binding.root
    }

}