package com.epf.android_project.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.epf.android_project.ui.MainActivity
import com.epf.android_project.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginActivity : AppCompatActivity() { // se connecter avec username: mor_2314 et mdp: 83r5^_

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameField = findViewById<EditText>(R.id.usernameInput)
        val passwordField = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerText = findViewById<TextView>(R.id.goToRegister)

        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                login(username, password)
            } else {
                Toast.makeText(this, "Remplis tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun login(username: String, password: String) {
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("https://fakestoreapi.com/auth/login")
            .post(body)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val token = JSONObject(responseBody).getString("token")
                runOnUiThread {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    val sharedPref = getSharedPreferences("auth", MODE_PRIVATE)
                    sharedPref.edit().putString("token", token).apply()

                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
