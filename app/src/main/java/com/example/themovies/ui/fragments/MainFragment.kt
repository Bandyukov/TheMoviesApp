package com.example.themovies.ui.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.themovies.R
import com.example.themovies.core.network.Connection
import com.example.themovies.core.MoviesApi
import com.example.themovies.core.database.database_itself.MovieDatabase
import com.example.themovies.core.repo.MainRepository
import com.example.themovies.databinding.FragmentMainBinding
import com.example.themovies.ui.recycler.movie.MovieAdapter
import com.example.themovies.ui.recycler.movie.OnMovieClickListener
import com.example.themovies.ui.recycler.movie.OnReachEndListener


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private var isLoading = false

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
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        binding.lifecycleOwner = this

        movieAdapter = MovieAdapter()

        binding.recyclerViewMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(this.context, displayMatrix())
        }

        MovieAdapter.setOnMovieClickListener(object: OnMovieClickListener {
            override fun onClick(position: Int) {
                mainViewModel.getMovie(mainViewModel.listOfMovies.value!![position].uniqueId)

                val bundle = Bundle()
                bundle.putInt("id", mainViewModel.listOfMovies.value!![position].uniqueId)

                findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
            }
        })

        movieAdapter.setOnReachEndListener(object : OnReachEndListener {
            override fun onReachEnd() {
                if (Connection.isInternetConnection && MainRepository.currentPage == mainViewModel.page.value) {
                    mainViewModel.increasePage()
                    Log.i("page", "$///////////////////////${mainViewModel.page.value}")
                    getData()
                }
            }
        })

        mainViewModel.listOfMovies.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                Log.i("cv", "list is null or empty")
                binding.imageViewNoInternet.visibility = View.VISIBLE
                Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_LONG)
                    .show()
            }
            MainViewModel.flag = true
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

        if (mainViewModel.listOfMovies.value == null) { getData() }

        setHasOptionsMenu(true)

        return binding.root
    }

    fun getData() {
        mainViewModel.getAllMovies()
    }

    private fun initialization() {
        binding.imageViewNoInternet.visibility = View.INVISIBLE
        Connection.isInternetConnection = true
        if (mainViewModel.listOfMovies.value.isNullOrEmpty()) {
            mainViewModel.setDefaultPage()
            getData()
        }

    }

    private fun displayMatrix() : Int {
        val display = DisplayMetrics()
        val width = (display.widthPixels / display.density).toInt()
        if (width / 185 > 2)
            return width / 185
        return 2
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favoriteMovies -> findNavController().navigate(R.id.action_mainFragment_to_favoriteMoviesFragment)
            R.id.homeScreen -> Toast.makeText(requireContext(), "Here", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}