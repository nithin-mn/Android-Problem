package com.example.android.problem

import com.google.gson.annotations.SerializedName

data class UserRequest(val emailId: String)

data class UserResponse(
        @SerializedName("emailId") val emailId: String,
        @SerializedName("imageUrl") val imageUrl: String,
        @SerializedName("firstName") val firstName: String,
        @SerializedName("lastName") val lastName: String
)

data class TotalResponse(@SerializedName("items") val items: List<UserResponse>)



