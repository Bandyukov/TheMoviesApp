package com.example.themovies.core

import com.example.themovies.core.models.movie.RequestResponseVO
import com.example.themovies.core.models.review.ReviewsRequestVO
import com.example.themovies.core.models.trailer.TrailerRequestVO
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private const val API_KEY = "c4711720f8c269eff09b8acc50d352a8"
private const val BASE_URL = "https://api.themoviedb.org/"
//https://api.themoviedb.org/3/discover/movie?api_key=c4711720f8c269eff09b8acc50d352a8

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MoviesApiService {
//    @GET("3/discover/movie?api_key=c4711720f8c269eff09b8acc50d352a8&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false")
//    suspend fun getMoviesFromPage(@Query(value = "page") page: Int) : RequestResponseVO

    @GET("3/discover/movie?api_key=c4711720f8c269eff09b8acc50d352a8&language=en-US")
    suspend fun getMoviesFromNet(
        @Query(value = "sort_by") sortBy: String,
        @Query(value = "include_adult") includeAdult: Boolean,
        @Query(value = "include_video") includeVideo: Boolean,
        @Query(value = "page") page: Int,
        @Query(value = "vote_count.gte") votes: Int
    ) : RequestResponseVO

    @GET("3/movie/{id}/videos?api_key=c4711720f8c269eff09b8acc50d352a8&language=en-US")
    suspend fun getTrailers(@Path("id") id: Int): TrailerRequestVO

    @GET("3/movie/{id}/reviews?api_key=c4711720f8c269eff09b8acc50d352a8&language=en-US")
    suspend fun getReviews(@Path("id") id: Int, @Query(value = "page") page: Int) : ReviewsRequestVO

}

object MoviesApi {
    val moviesApiService : MoviesApiService by lazy {
        retrofit.create(MoviesApiService::class.java)
    }
}