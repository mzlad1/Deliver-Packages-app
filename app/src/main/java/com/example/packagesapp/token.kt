package com.example.packagesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.packagesapp.databinding.ActivityTokenBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class token : AppCompatActivity() {
    lateinit var binding : ActivityTokenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(baseContext, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show()
                return@OnCompleteListener
            }
            val token = task.result
            print(token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()

            binding.tvToken.text = token
        })
    }
}