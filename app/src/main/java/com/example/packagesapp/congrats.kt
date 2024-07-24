package com.example.packagesapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.packagesapp.databinding.ActivityCongratsBinding

class congrats : AppCompatActivity() {
    lateinit var binding : ActivityCongratsBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCongratsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("loginPref", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        var message = "Welcome To logestechs"
        binding.txtCongrats.text = message
        binding.btnToLogin.setOnClickListener {
            intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}