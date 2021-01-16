package com.example.themovies.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.GridLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.themovies.R
import com.example.themovies.core.MoviesApi
import com.example.themovies.core.database.database_itself.MovieDatabase
import com.example.themovies.core.preferences.AppPreferences
import com.example.themovies.core.repo.MainRepository
import com.example.themovies.databinding.FragmentFavoriteMoviesBinding
import com.example.themovies.ui.recycler.movie.MovieAdapter
import com.example.themovies.ui.recycler.movie.OnMovieClickListener
import com.example.themovies.ui.recycler.movie.OnReachEndListener

class FavoriteMoviesFragment : Fragment() {

    private val mainViewModel by lazy {
        val database = MovieDatabase.getInstance(requireContext().applicationContext)
        val appPreferences = AppPreferences.getInstance(requireContext().applicationContext)
        val repository = MainRepository(MoviesApi.moviesApiService, database.movieDao, appPreferences)
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

        mainViewModel.getAllMoviesFromFavoriteDB()

        binding.lifecycleOwner = this

        val movieAdapter = MovieAdapter()
        binding.recyclerViewFavoriteMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(requireContext().applicationContext, 2)
        }

        mainViewModel.listOfFavoriteMovies.observe(viewLifecycleOwner) {
            movieAdapter.addListOfMovies(it)
        }

        MovieAdapter.setOnMovieClickListener(object : OnMovieClickListener {
            override fun onClick(position: Int) {
                val id = mainViewModel.listOfFavoriteMovies.value!![position].id
                mainViewModel.getMovie(id)

                val bundle = Bundle()
                bundle.putInt("id", id)

                findNavController().navigate(R.id.action_favoriteMoviesFragment_to_detailFragment, bundle)
            }

            override fun onLongClick(position: Int) {
                mainViewModel.addOrDelete(position, true, requireContext().applicationContext)
            }
        })

        movieAdapter.setOnReachEndListener(object : OnReachEndListener {
            override fun onReachEnd() {

            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            R.id.deleteFromFavorite -> {
                mainViewModel
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
