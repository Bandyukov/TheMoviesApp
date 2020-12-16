package com.example.themovies.core

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
    @GET("3/discover/movie?api_key=c4711720f8c269eff09b8acc50d352a8&language=en-US&sort_by=revenue.desc&include_adult=false&include_video=false")
    suspend fun getMoviesFromPage(@Query(value = "page") page: Int) : RequestResponseVO

}

object MoviesApi {
    val moviesApiService : MoviesApiService by lazy {
        retrofit.create(MoviesApiService::class.java)
    }
}