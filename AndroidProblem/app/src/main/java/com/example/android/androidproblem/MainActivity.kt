package com.example.android.androidproblem

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val emailId by lazy { findViewById<EditText>(R.id.email_id) }
    val userList by lazy { findViewById<ListView>(R.id.user_list) }
    val done by lazy { findViewById<Button>(R.id.done_button) }
    val userString = ArrayList<String>()
    val listOfImageUrl = ArrayList<String>()
    val apiService by lazy { RestApiService() }
    val gson = Gson()
    lateinit var sharedPref:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch{
            getCache()
        }
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
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        val userString = sharedPref.getString("user_string", null)
        val urlString = sharedPref.getString("url_string", null)
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val userJson = gson.fromJson<ArrayList<String>>(userString, type)
        val urlJson = gson.fromJson<ArrayList<String>>(urlString, type)
        if (userString != null && urlString != null) {
            createListView(userJson, urlJson)
        }
    }

    private fun getHttpResponse() {
        val userInfo = UserRequest(emailId.text.toString())
        apiService.addUser(userInfo) {
            if (it?.items != null) {
                setView(it)
            }
        }
    }

    private fun setView(userlist: UserList) {
        userlist.items.forEach {
            userString.add(it.toString())
            listOfImageUrl.add(it.imageUrl)
        }
        createListView(userString, listOfImageUrl)
    }

    private fun updateCache() {
        GlobalScope.launch {
            sharedPref.edit().putString("user_string", gson.toJson(userString)).apply()
            sharedPref.edit().putString("url_string", gson.toJson(listOfImageUrl)).apply()
        }
    }

    private fun createListView(userString: ArrayList<String>, urlList: ArrayList<String>) {
        userList.adapter = CustomAdapter(this, userString, urlList)
    }

}
private const val TAG = "MainActivity"
