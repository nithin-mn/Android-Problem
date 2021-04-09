package com.example.android.androidproblem


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private val client by lazy { OkHttpClient.Builder().build() }
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://surya-interview.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
