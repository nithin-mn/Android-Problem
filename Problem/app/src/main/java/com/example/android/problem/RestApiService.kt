package com.example.android.problem

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestApiService {
    fun addUser(userData: UserRequest, onResult: (TotalResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addUser(userData).enqueue(
                object : Callback<TotalResponse> {
                    override fun onFailure(call: Call<TotalResponse>, t: Throwable) {

                        Log.d("RestApiService", "failure callback:$t")
                        onResult(null)
                    }

                    override fun onResponse(call: Call<TotalResponse>, response: Response<TotalResponse>) {
                        val addedUser = response.body()
                        Log.d("RestApiService", addedUser.toString())
                        onResult(addedUser)
                    }
                }
        )
    }
}