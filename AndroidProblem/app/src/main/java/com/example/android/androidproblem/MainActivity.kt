package com.example.android.androidproblem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    val emailId by lazy { findViewById<EditText>(R.id.email_id) }
    val userList by lazy { findViewById<ListView>(R.id.user_list) }
    val done by lazy { findViewById<Button>(R.id.done_button) }
    val userString = ArrayList<String>()
    val listOfImageUrl = ArrayList<String>()
    val apiService by lazy { RestApiService() }
    val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCache()
        done.setOnClickListener {
            if (emailId.text.isNullOrEmpty()) {
                Log.d(TAG, "invalid email")
            } else {
                getHttpResponse()
                updateCache()
            }
        }

    }

    private fun getCache() {
        val userString = sharedPref.getString("user_string", "")
        val urlString = sharedPref.getString("url_string", "")
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val userJson = gson.fromJson<ArrayList<String>>(userString, type)
        val urlJson = gson.fromJson<ArrayList<String>>(urlString, type)
        if (userString != "" && urlString != "") {
            createListView(userJson, urlJson)
        }
    }

    private fun getHttpResponse() {
        Log.d(TAG, "gethttpresponse")
        val userInfo = UserRequest(emailId.text.toString())
        Log.d(TAG, "request")
        apiService.addUser(userInfo) {
            if (it?.items != null) {
                setView(it)
            } else {
                Log.d(TAG, "noresponse")
            }
        }
    }

    private fun setView(userlist: UserList) {
        Log.d(TAG, "gotresponse")
        userlist.items.forEach {
            userString.add(it.toString())
            listOfImageUrl.add(it.imageUrl)
        }
        createListView(userString, listOfImageUrl)
    }


    private fun updateCache() {
        Log.d(TAG, "updatecache")
        sharedPref.edit().putString("user_id", gson.toJson(userString)).apply()
        sharedPref.edit().putString("image_url", gson.toJson(listOfImageUrl)).apply()
    }

    private fun createListView(userString: ArrayList<String>, urlList: ArrayList<String>) {
        userList.adapter = CustomAdapter(this, userString, urlList)
    }

}
private const val TAG = "MainActivity"
