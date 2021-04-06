package com.example.android.problem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.GsonBuilder


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OnCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val responseText = findViewById<TextView>(R.id.response_text)
        val emailId = findViewById<EditText>(R.id.email_id)

        val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        val userId = sharedPref.getString("user_id", "EMPTY")
        responseText.text = userId

        findViewById<Button>(R.id.done_button).setOnClickListener {

            Log.d("MainActivity", "OnClick called")

            val gsonPretty = GsonBuilder().setPrettyPrinting().create()

            val apiService = RestApiService()
            val userInfo = UserRequest(emailId.text.toString())



            apiService.addUser(userInfo) {
                if (it != null) {
                    Log.d(TAG, "POST REQUEST SUCCESSFUL")
                    val items = gsonPretty.toJson(it)
                    responseText.text = items
                    sharedPref.edit().putString("user_id", items).apply()

                } else {
                    Log.d(TAG, "POST REQUEST FAILURE")
                }
            }
        }
    }
}