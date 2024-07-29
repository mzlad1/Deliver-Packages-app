package com.example.packagesapp

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Color.alpha
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.renderscript.ScriptGroup.Binding
import com.example.packagesapp.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var Binding: ActivitySplashScreenBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        logoAnimation()

}

    fun logoAnimation(){
        Handler().postDelayed({
            checkSharedPreferences()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            Binding.imageView.animate().apply {
                duration = 1000
                alpha(1f)
            }.start()
        }, 2000)

    }
    fun checkSharedPreferences(){
        sharedPreferences = getSharedPreferences("loginPref", MODE_PRIVATE)
        var isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

}