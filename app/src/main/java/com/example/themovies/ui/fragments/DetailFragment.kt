package com.example.themovies.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovies.R
import com.example.themovies.core.MoviesApi
import com.example.themovies.core.database.database_itself.MovieDatabase
import com.example.themovies.core.models.movie.Movie
import com.example.themovies.core.models.trailer.Trailer
import com.example.themovies.core.preferences.AppPreferences
import com.example.themovies.core.repo.MainRepository
import com.example.themovies.databinding.FragmentDetailBinding
import com.example.themovies.ui.recycler.review.ReviewAdapter
import com.example.themovies.ui.recycler.trailer.OnTrailerClickListener
import com.example.themovies.ui.recycler.trailer.TrailerAdapter
import com.squareup.moshi.ToJson
import com.squareup.picasso.Picasso


class DetailFragment : Fragment() {

    private val mainViewModel by lazy {
        val database = MovieDatabase.getInstance(requireContext().applicationContext)
        val appPreferences = AppPreferences.getInstance(requireContext().applicationContext)
        val repo = MainRepository(MoviesApi.moviesApiService, database.movieDao, appPreferences)
        val mainViewModelFactory = MainViewModelFactory(repo)
        ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
    }

    private var isFavorite: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )

        isFavorite = false

        binding.lifecycleOwner = this

        val trailerAdapter = TrailerAdapter()
        binding.recyclerViewTrailers.apply {
            adapter = trailerAdapter
            layoutManager = LinearLayoutManager(requireContext().applicationContext)
        }

        val reviewAdapter = ReviewAdapter()
        binding.recyclerViewReviews.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(requireContext().applicationContext)
        }

        val bundle: Bundle? = this.arguments

        bundle?.let { bun ->
            mainViewModel.getMovie(bun.getInt("id"))

            mainViewModel.movie.observe(viewLifecycleOwner) {
                val address = Movie.BASE_POSTER_URL + Movie.BIG_POSTER_SIZE + it.posterPath
                Picasso.get().load(address).into(binding.imageViewBigPoster)
                binding.textViewOriginalTitleMovie.text = it.originalTitle
                binding.textViewReleaseDateMovie.text = it.releaseDate
                binding.textViewOverviewMovie.text = it.overview
                binding.textViewRatingMovie.text = it.rating.toString()
                binding.textViewVoteCount.text = it.voteCount.toString()

                mainViewModel.getTrailers()
                mainViewModel.getReviews()
            }

            mainViewModel.movieFav.observe(viewLifecycleOwner) {
                isFavorite = if (it == null) {
                    false
                } else {
                    mainViewModel.movie.value?.id == mainViewModel.movieFav.value?.id
                }
                Log.i("fav", "1) ${mainViewModel.movie.value?.id}")
                Log.i("fav", "1) ${mainViewModel.movie.value?.uniqueId}")
                Log.i("fav", "2) ${mainViewModel.movieFav.value?.id}")
                Log.i("fav", "2) ${mainViewModel.movieFav.value?.uniqueId}")
                Log.i("fav", "$isFavorite")

            }

            mainViewModel.listOfTrailers.observe(viewLifecycleOwner) {
                trailerAdapter.setTrailers(it)
            }

            mainViewModel.listOfReview.observe(viewLifecycleOwner) {
                reviewAdapter.setReviews(it)
            }

            TrailerAdapter.setOnTrailerClickListener(object : OnTrailerClickListener {
                override fun onShortClick(position: Int) {
                    val intent = Intent(
                        Intent.ACTION_VIEW, (Trailer.YOUTUBE_BASE_URL +
                                mainViewModel.listOfTrailers.value!![position].key).toUri()
                    )
                    startActivity(intent)
                }

                override fun onLongClick(position: Int) {
                    val s = Trailer.YOUTUBE_BASE_URL +
                            mainViewModel.listOfTrailers.value!![position].key
                    Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
                }
            })
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.makeFavorite -> {
                val message = if (isFavorite) {
                    getString(R.string.is_favorite)
                } else {
                    mainViewModel.addToFavorite()
                    isFavorite = true
                    getString(R.string.movie_added)
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
            R.id.goToMainFragment -> mainViewModel.clearFavoriteDB()
        }
        return super.onOptionsItemSelected(item)
    }

}