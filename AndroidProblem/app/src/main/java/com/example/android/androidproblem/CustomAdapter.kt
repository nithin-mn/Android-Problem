package com.example.android.androidproblem

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest

class CustomAdapter(
    private val context: Activity,
    private val description: ArrayList<String>,
    private val imageurl: ArrayList<String>
) : ArrayAdapter<String>(context, R.layout.user_item, description) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.user_item, null, true)

        val userDetail: TextView = rowView.findViewById(R.id.user_detail)
        val userImage: ImageView = rowView.findViewById(R.id.user_image)
        userDetail.text = description[position]

        val request = ImageRequest.Builder(context)
            .data(imageurl[position])
            .target(
                onStart = { placeholder ->
                    userImage.load(placeholder)
                },
                onSuccess = { result ->
                    userImage.setImageDrawable(result)
                },
                onError = { error ->
                    userImage.load(R.drawable.default_image)
                }
            )
            .build()

        val imageLoader = ImageLoader(context)
        imageLoader.enqueue(request)
        return rowView
    }
}
