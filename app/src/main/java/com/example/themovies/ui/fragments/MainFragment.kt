package com.example.themovies.ui.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.themovies.R
import com.example.themovies.core.network.Connection
import com.example.themovies.core.MoviesApi
import com.example.themovies.core.database.database_itself.MovieDatabase
import com.example.themovies.core.preferences.AppPreferences
import com.example.themovies.core.repo.MainRepository
import com.example.themovies.databinding.FragmentMainBinding
import com.example.themovies.ui.recycler.movie.MovieAdapter
import com.example.themovies.ui.recycler.movie.OnMovieClickListener
import com.example.themovies.ui.recycler.movie.OnReachEndListener
import java.util.*


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var  methodOfSort: Array<String>
    private var isLoading = false

    companion object {
        private const val GROUP_ID = 201
        private val IDs = Array(4) { 100 + it }
    }

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
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        binding.lifecycleOwner = this

        methodOfSort = resources.getStringArray(R.array.method_of_sort)

        movieAdapter = MovieAdapter()

        binding.recyclerViewMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(this.context, displayMatrix())
        }

        MovieAdapter.setOnMovieClickListener(object: OnMovieClickListener {
            override fun onClick(position: Int) {
                val id = mainViewModel.listOfMovies.value!![position].id

                val bundle = Bundle()
                bundle.putInt("id", id)

                Log.i("fav", "selected movie id is $id")

                findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
            }

            override fun onLongClick(position: Int) {
                Toast.makeText(requireContext(), "$position", Toast.LENGTH_SHORT).show()
            }
        })

        movieAdapter.setOnReachEndListener(object : OnReachEndListener {
            override fun onReachEnd() {
                if (Connection.isInternetConnection && MainRepository.currentPage == mainViewModel.page.value) {
                    mainViewModel.increasePage()
                    getData(mainViewModel.sort.value!!)
                }
            }
        })

        mainViewModel.listOfMovies.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
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

        if (mainViewModel.listOfMovies.value == null) {
            mainViewModel.setDefaultPage()
            getData(mainViewModel.sort.value!!) }

        setHasOptionsMenu(true)

        return binding.root
    }


    fun getData(methodOfSort: Int) {
        mainViewModel.getAllMovies(methodOfSort)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val subMenu = menu.addSubMenu(getString(R.string.sort_by))
        subMenu.add(GROUP_ID, IDs[0], Menu.NONE, methodOfSort[0])
        subMenu.add(GROUP_ID, IDs[1], Menu.NONE, methodOfSort[1])
        subMenu.add(GROUP_ID, IDs[2], Menu.NONE, methodOfSort[2])
        subMenu.add(GROUP_ID, IDs[3], Menu.NONE, methodOfSort[3])
        subMenu.setGroupCheckable(GROUP_ID, true, true) // 3ий аргумент false когда хотим чекбокс

        mainViewModel.sort.value?.let {
            subMenu[it].isChecked = true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favoriteMovies -> findNavController().navigate(R.id.action_mainFragment_to_favoriteMoviesFragment)
            R.id.homeScreen -> Toast.makeText(requireContext().applicationContext,
                "It is home screen ${R.id.homeScreen}",
                Toast.LENGTH_SHORT)
                .show()
            IDs[0] -> check(item, 0)
            IDs[1] -> check(item, 1)
            IDs[2] -> check(item, 2)
            IDs[3] -> check(item, 3)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun check(item: MenuItem, sort: Int) {
        item.isChecked = !item.isChecked
        mainViewModel.setMethodOfSort(sort)
        mainViewModel.setDefaultValues()
        getData(sort)
    }

    private fun initialization() {
        binding.imageViewNoInternet.visibility = View.INVISIBLE
        Connection.isInternetConnection = true
        if (mainViewModel.listOfMovies.value.isNullOrEmpty()) {
            mainViewModel.setDefaultPage()
            getData(mainViewModel.sort.value!!)
        }

    }

    private fun displayMatrix() : Int {
        val display = DisplayMetrics()
        val width = (display.widthPixels / display.density).toInt()
        if (width / 185 > 2)
            return width / 185
        return 2
    }
}