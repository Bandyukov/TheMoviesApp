package com.example.themovies.ui.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.themovies.R
import com.example.themovies.core.network.Connection
import com.example.themovies.core.MoviesApi
import com.example.themovies.core.database.database_itself.MovieDatabase
import com.example.themovies.core.repo.MainRepository
import com.example.themovies.databinding.FragmentMainBinding
import com.example.themovies.ui.recycler.MovieAdapter
import com.example.themovies.ui.recycler.OnReachEndListener


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private var isLoading = false

    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        binding.lifecycleOwner = this

        val database = MovieDatabase.getInstance(requireContext().applicationContext)
        val repository = MainRepository(MoviesApi.moviesApiService, database.movieDao)

        val viewModelFactory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        movieAdapter = MovieAdapter()

        binding.recyclerViewMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(this.context, displayMatrix())
        }

        movieAdapter.setOnReachEndListener(object : OnReachEndListener {
            override fun onReachEnd() {
                if (Connection.isInternetConnection) {
                    mainViewModel.increasePage()
                    if (mainViewModel.page.value == 3)
                        mainViewModel.increasePage()
                    mainViewModel.getAllMoviesFromNetwork()
                }
            }
        })

        mainViewModel.listOfMovies.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                binding.imageViewNoInternet.visibility = View.VISIBLE
                Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_LONG)
                    .show()
            }
            movieAdapter.addListOfMovies(it)
        })

        mainViewModel.isLoadingGoing.observe(viewLifecycleOwner, {
            if (it)
                binding.progressBarLoading.visibility = View.VISIBLE
            else
                binding.progressBarLoading.visibility = View.INVISIBLE
        })

        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = true
            initialization()
            binding.refreshLayout.isRefreshing = false
        }

        getData()

        return binding.root
    }

    fun getData() {
        mainViewModel.getAllMovies()
    }

    private fun initialization() {
        binding.imageViewNoInternet.visibility = View.INVISIBLE
        movieAdapter = MovieAdapter()
        binding.recyclerViewMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(this.context, displayMatrix())
        }
        mainViewModel.setDefaultPage()
        Connection.isInternetConnection = true
        getData()

        movieAdapter.setOnReachEndListener(object : OnReachEndListener {
            override fun onReachEnd() {
                if (Connection.isInternetConnection) {
                    mainViewModel.increasePage()
                    if (mainViewModel.page.value == 3)
                        mainViewModel.increasePage()
                    mainViewModel.getAllMoviesFromNetwork()
                }
            }
        })
    }

    fun displayMatrix() : Int {
        val display = DisplayMetrics()
        val width = (display.widthPixels / display.density).toInt()
        if (width / 185 > 2)
            return width / 185
        return 2
    }
}