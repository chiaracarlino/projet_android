package com.epf.android_project.ui.auth

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.epf.android_project.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Inscription"

        val username = findViewById<EditText>(R.id.registerUsername)
        val password = findViewById<EditText>(R.id.registerPassword)
        val email = findViewById<EditText>(R.id.registerEmail)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()
            val mail = email.text.toString()

            if (user.isBlank() || pass.isBlank() || mail.isBlank()) {
                Toast.makeText(this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            register(user, pass, mail)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun register(username: String, password: String, email: String) {
        val json = JSONObject().apply {
            put("email", email)
            put("username", username)
            put("password", password)
            put("name", JSONObject().apply {
                put("firstname", "John")
                put("lastname", "Doe")
            })
            put("address", JSONObject().apply {
                put("city", "Paris")
                put("street", "Rue Exemple")
                put("number", 123)
                put("zipcode", "75000")
                put("geolocation", JSONObject().apply {
                    put("lat", "0")
                    put("long", "0")
                })
            })
            put("phone", "0102030405")
        }

        val request = Request.Builder()
            .url("https://fakestoreapi.com/users")
            .post(json.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Inscription réussie !", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
