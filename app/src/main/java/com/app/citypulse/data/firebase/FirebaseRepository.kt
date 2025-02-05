package com.example.pruebas.data

import com.app.citypulse.data.dataUsers.UserItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()
    private  val collectionRef = db.collection("Usuarios")

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