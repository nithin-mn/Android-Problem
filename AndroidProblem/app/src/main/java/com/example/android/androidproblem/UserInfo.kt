package com.example.android.androidproblem

import com.google.gson.annotations.SerializedName

data class UserRequest(val emailId: String)

data class User(
    @SerializedName("emailId") val emailId: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String
)

data class UserList(@SerializedName("items") val items: List<User>)
