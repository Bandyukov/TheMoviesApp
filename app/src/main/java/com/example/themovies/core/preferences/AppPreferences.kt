package com.example.themovies.core.preferences

import android.content.Context
import android.content.SharedPreferences

class AppPreferences private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getPage(): Int = sharedPreferences.getInt(PAGE_PARAM, 1)

    fun getSort(): Int = sharedPreferences.getInt(SORT_PARAM, 0)

    fun setPage(page: Int) = sharedPreferences.edit().putInt(PAGE_PARAM, page).apply()

    fun setSort(sort: Int) = sharedPreferences.edit().putInt(SORT_PARAM, sort).apply()

    companion object {
        private const val PREFERENCES_NAME = "MoviesInfo"
        private const val PAGE_PARAM = "page"
        private const val SORT_PARAM = "sort"

        private var INSTANCE: AppPreferences? = null

        fun getInstance(context: Context) : AppPreferences {
            var instance = INSTANCE
            if (instance == null) {
                instance = AppPreferences(context)
                INSTANCE = instance
            }
            return instance
        }
    }

}