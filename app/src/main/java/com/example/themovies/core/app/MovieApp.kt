package com.example.themovies.core.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest

//class MovieApp : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        val context = applicationContext
//        val manager: ConnectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val builder = NetworkRequest.Builder()
//
//        manager.registerNetworkCallback(builder.build(), MyCallbacks())
//    }
//
//    override fun onTerminate() {
//        super.onTerminate()
//        val context = applicationContext
//        val manger: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        manger.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
//    }
//}