package com.example.packagesapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore


lateinit var db : FirebaseFirestore
class DatabaseHelper {

    fun loadPackages(context: Context, callback: PackagesCallback) {
        val db = FirebaseFirestore.getInstance()
        val sharedPreferences = context.getSharedPreferences("loginPref", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        if (username == null) {
            Toast.makeText(context, "Login required", Toast.LENGTH_SHORT).show()
            return
        }
        db.collection("users").whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("users").document(document.id).collection("packages").get()
                        .addOnSuccessListener { documents ->
                            val packages = mutableListOf<Package>()
                            for (document in documents) {
                                val newPackage = Package(
                                    document.id,
                                    document.getString("receiver")!!,
                                    document.getString("sender")!!,
                                    document.getString("receiverPhone")!!,
                                    document.getString("price")!!.toDoubleOrNull() ?: 0.0
                                )
                                packages.add(newPackage)
                            }
                            callback.onCallback(packages)
                        }
                }
            }
    }

    fun addPackage(context: Context, username: String, newPackage: Package) {
        val db = FirebaseFirestore.getInstance()
        val newPackageMap = hashMapOf(
            "receiver" to newPackage.receiver,
            "sender" to newPackage.sender,
            "receiverPhone" to newPackage.receiverPhone,
            "price" to newPackage.price
        )

        db.collection("users").whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("users").document(document.id).collection("packages").add(newPackageMap)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(context, "Package added Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error adding package: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }

    fun getUserInfo(username: String, password: String, callback: UserInfoCallback) {
        db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("username", username).whereEqualTo("password", password).get()
            .addOnSuccessListener { documents ->
                var userInfo = UserInfo("","","","","","") // Default to empty
                for (document in documents) {
                    userInfo = UserInfo(
                        document.getString("username")!!,
                        document.getString("email")!!,
                        document.getString("password")!!,
                        document.getString("name")!!,
                        document.getString("phone")!!,
                        document.getString("address")!!
                    )
                }
                callback.onCallback(userInfo)
            }
    }

    fun addUser(context: Context,newUser: UserInfo){
        db = FirebaseFirestore.getInstance()
        val newUserMap = hashMapOf(
            "username" to newUser.username,
            "email" to newUser.email,
            "password" to newUser.password,
            "name" to newUser.name,
            "phone" to newUser.phone,
            "address" to newUser.address
        )
        db.collection("users").add(newUserMap)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "User added Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
        Toast.makeText(context, "Error adding user: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun getPackageDetails(packageId: String, callback: PackageDetailsCallback) {
        db = FirebaseFirestore.getInstance()
        db.collection("packages").document(packageId).get()
            .addOnSuccessListener { document ->
                val packageDetails = Package(
                    document.getString("receiver") ?: "Unknown",
                    document.getString("sender") ?: "Unknown",
                    document.getString("receiverPhone") ?: "Unknown",
                    document.getDouble("price") ?: 0.0
                )

                callback.onCallback(packageDetails)
            }
    }

    fun deletePackage(packageId: String,username :String, context: Context) {
        db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("users").document(document.id).collection("packages").document(packageId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Package delivered Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error deleting package: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }

}