package com.example.android.androidproblem

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestApiService {
    fun addUser(userData: UserRequest, onResult: (UserList?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addUser(userData).enqueue(
            object : Callback<UserList> {
                override fun onFailure(call: Call<UserList>, t: Throwable) {
                    Log.d("RestApiService", "failure callback:$t")
                    onResult(null)
                }
                override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                    val addedUser = response.body()
                    Log.d("RestApiService", addedUser.toString())
                    onResult(addedUser)
                }
            }
        )
    }
}
