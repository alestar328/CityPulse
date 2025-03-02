package com.app.citypulse.data.repository

import com.app.citypulse.data.dataUsers.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private  val collectionRef = db.collection("users")
    private val auth = FirebaseAuth.getInstance()

    suspend fun addUser (User: UserItem){
        collectionRef.add(User).await()
    }

    suspend fun getUsers(): List<UserItem>{
        return collectionRef.get().await().toObjects(UserItem::class.java)
    }

    suspend fun updateUser(id:String, newUser: UserItem){
        collectionRef.document(id).set(newUser).await()
    }

    suspend fun deleteUser(id: String){
        collectionRef.document(id).delete().await()
    }

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