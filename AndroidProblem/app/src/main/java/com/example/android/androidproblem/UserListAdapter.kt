package com.example.android.androidproblem


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest

class UserListAdapter(
    private val contex: Context,
    private val description: List<User>,
) : ArrayAdapter<User>(contex, R.layout.user_item, description) {
    private val imageLoader = ImageLoader(context)

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = LayoutInflater.from(contex).inflate(R.layout.user_item, null, true)
        val userDetail: TextView = rowView.findViewById(R.id.user_detail)
        val userImage: ImageView = rowView.findViewById(R.id.user_image)
        userDetail.text =
            "${description[position].firstName} ${description[position].lastName}\n${description[position].emailId}"
        val request = ImageRequest.Builder(contex)
            .data(description[position].imageUrl)
            .target(
                onStart = {
                    userImage.load(R.drawable.default_image)
                },
                onSuccess = { result ->
                    userImage.setImageDrawable(result)
                },
                onError = {
                    userImage.load(R.drawable.default_image)
                }
            )
            .build()

        imageLoader.enqueue(request)
        return rowView
    }
}
