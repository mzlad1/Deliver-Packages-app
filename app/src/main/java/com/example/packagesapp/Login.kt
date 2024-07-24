package com.example.packagesapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.packagesapp.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    lateinit var Binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        Binding.signupbutton.setOnClickListener {
            startActivity(Intent(this, signUp::class.java))
        }

        Binding.loginbutton.setOnClickListener {
            val username = Binding.username.text.toString().trim()
            val password = Binding.password.text.toString().trim()
            if (username.isEmpty()) {
                Binding.username.error = "Username is required"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Binding.password.error = "Password is required"
                return@setOnClickListener
            }

            val db = DatabaseHelper()
            db.getUserInfo(username, password, object : UserInfoCallback {
                override fun onCallback(userInfo: UserInfo) {
                    if (userInfo.username == "") {
                        Binding.username.error = "Username or password is incorrect"
                    } else {
                        sharedPreferences = getSharedPreferences("loginPref", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("username", userInfo.username)
                        editor.putString("email", userInfo.email)
                        editor.putString("name", userInfo.name)
                        editor.putString("phone", userInfo.phone)
                        editor.putString("address", userInfo.address)
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()

                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        }
}
}