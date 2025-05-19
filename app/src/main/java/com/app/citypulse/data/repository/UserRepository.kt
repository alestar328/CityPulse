package com.app.citypulse.data.repository

import com.app.citypulse.data.dataUsers.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private  val collectionRef = db.collection("users")
    private val auth = FirebaseAuth.getInstance()


    suspend fun getSavedEventIdsForUser(): List<String> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        val snaps = db.collection("users")
            .document(uid)
            .collection("savedEvents")
            .get()
            .await()
        return snaps.documents.mapNotNull { it.id }
    }
    suspend fun getAssistedEventIdsForUser(): List<String> {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return emptyList()
        val snaps = db
            .collection("users")
            .document(uid)
            .collection("assistedEvents")
            .get()
            .await()
        return snaps.documents.mapNotNull { it.getString("eventId") }
    }
    suspend fun saveEventForUser(eventId: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userDoc = db.collection("users").document(uid)
        userDoc
            .collection("savedEvents")
            .document(eventId)
            .set(mapOf("eventId" to eventId))
            .await()

    }

    suspend fun assistedEventForUser(eventId: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db
            .collection("users")
            .document(uid)
            .collection("assistedEvents")
            .document(eventId)
            .set(mapOf("eventId" to eventId, "timestamp" to FieldValue.serverTimestamp()))
            .await()
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

    fun saveLanguagePreference(language: String) {
        val userId = auth.currentUser?.uid
        userId?.let {
            db.collection("users").document(it)
                .update("language", language)
        }
    }
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


}