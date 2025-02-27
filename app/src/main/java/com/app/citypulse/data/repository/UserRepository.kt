package com.app.citypulse.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveLanguagePreference(language: String) {
        val userId = auth.currentUser?.uid
        userId?.let {
            db.collection("users").document(it)
                .update("language", language)
        }
    }

    fun getLanguagePreference(callback: (String) -> Unit) {
        val userId = auth.currentUser?.uid
        userId?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    val language = document.getString("language") ?: "es"
                    callback(language)
                }
        }
    }
}