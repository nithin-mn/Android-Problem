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
    private val type by lazy { object : TypeToken<ArrayList<User>>() {}.type }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch { getCache() }
        done.setOnClickListener {
            if (emailId.text.isNullOrEmpty()) {
                Toast.makeText(this, "Enter valid email id", Toast.LENGTH_SHORT).show()
            } else {
                getHttpResponse()
            }
        }
    }

    private fun getCache() {
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        val userString = sharedPref.getString("user_string", null)
        if (userString != null) {
            createListView(gson.fromJson<ArrayList<User>>(userString, type))
        }
    }

    private fun getHttpResponse() {
        val userInfo = UserRequest(emailId.text.toString())
        apiService.addUser(userInfo) { userList1 ->
            userList1?.items.let {
                if (it != null) {
                    createListView(it)
                }
                sharedPref.edit().putString("user_string", gson.toJson(it)).apply()
            }
        }
    }

    private fun createListView(userString: List<User>) {
        userList.adapter = CustomAdapter(this, userString)
    }
}
