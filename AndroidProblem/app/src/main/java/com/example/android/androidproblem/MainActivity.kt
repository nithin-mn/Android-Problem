package com.example.android.androidproblem

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val emailId by lazy { findViewById<EditText>(R.id.email_id) }
    private val userList by lazy { findViewById<ListView>(R.id.user_list) }
    private val done by lazy { findViewById<Button>(R.id.done_button) }
    private val apiService by lazy { RestApiService() }
    private val gson = Gson()
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch { getCache() }
        done.setOnClickListener {
            if (emailId.text.isNullOrEmpty()) {
                Toast.makeText(this,"Enter valid email id",Toast.LENGTH_SHORT).show()
            } else {
                getHttpResponse()
            }
        }
    }

    private fun getCache() {
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        val userString = sharedPref.getString("user_string", null)
        val type = object : TypeToken<ArrayList<User>>() {}.type
        val userJson = gson.fromJson<ArrayList<User>>(userString, type)
        if (userString != null) {
            createListView(userJson)
        }
    }

    private fun getHttpResponse() {
        val userInfo = UserRequest(emailId.text.toString())
        apiService.addUser(userInfo) {
            if (it?.items != null) {
                Log.d(TAG, "got response")
                createListView(it.items)
                sharedPref.edit().putString("user_string", gson.toJson(it.items)).apply()
            }
        }
    }

    private fun createListView(userString: List<User>) {
        userList.adapter = CustomAdapter(this, userString)
    }
}

private const val TAG = "MainActivity"
