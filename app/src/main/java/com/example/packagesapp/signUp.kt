package com.example.packagesapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.packagesapp.databinding.ActivitySignUpBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class signUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var db: FirebaseFirestore
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        db = Firebase.firestore
        binding.signupbutton.setOnClickListener {
            checkFields()
        }

    }

    fun checkFields() {
        val username = binding.username.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()
        val name = binding.name.text.toString().trim()
        val phone = binding.phone.text.toString().trim()
        val address = binding.address.text.toString().trim()
        val termsSwitch = binding.termsSwitch.isChecked
        var valid = true

        if (username.isEmpty()) {
            binding.username.error = "Username is required"
            valid = false
        }
        if (email.isEmpty()) {
            binding.email.error = "Email is required"
            valid = false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Invalid email address"
            valid = false
        }
        if (password.isEmpty()) {
            binding.password.error = "Password is required"
            valid = false
        } else if (password.length < 6 || !password.contains(Regex(".*[0-9].*")) || !password.contains(Regex(".*[!@#\$%^&*].*"))) {
            binding.password.error = "Password must be at least 6 characters long and contain at least one number and one special character"
            valid = false
        } else if (password != confirmPassword) {
            binding.password.error = "Passwords do not match"
            binding.confirmPassword.error = "Passwords do not match"
            valid = false
        }
        if (name.isEmpty()) {
            binding.name.error = "Name is required"
            valid = false
        }
        if (phone.isEmpty()) {
            binding.phone.error = "Phone number is required"
            valid = false
        }
        if (address.isEmpty()) {
            binding.address.error = "Address is required"
            valid = false
        }
        if (!termsSwitch) {
            binding.termsSwitch.error = "Please accept the terms and conditions"
            valid = false
        }


        if (valid) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").whereEqualTo("username", username).get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        binding.username.error = "Username already exists"
                        valid = false
                    } else {
                        db.collection("users").whereEqualTo("email", email).get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    binding.email.error = "Email already exists"
                                    valid = false
                                } else {
                                    if (valid) {
                                        val user = UserInfo(username, email, password, name, phone, address)
                                        val dbHelper = DatabaseHelper()
                                        dbHelper.addUser(this, user)
                                        sharedPreferences = getSharedPreferences("loginPref", MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        editor.putString("username", user.username)
                                        editor.putString("email", user.email)
                                        editor.putString("name", user.name)
                                        editor.putString("phone", user.phone)
                                        editor.putString("address", user.address)
                                        editor.putBoolean("isLoggedIn", true)
                                        editor.apply()

                                        val intent = Intent(this@signUp, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                            .addOnFailureListener {
                                Log.d("TAG", "Error getting documents: ", it)
                            }
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Error getting documents: ", it)
                }
        }
    }

}