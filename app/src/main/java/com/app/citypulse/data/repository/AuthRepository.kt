package com.app.citypulse.data.repository

import com.app.citypulse.data.model.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Método para obtener FirebaseAuth
    fun getFirebaseAuth(): FirebaseAuth {
        return auth
    }

    // Método para obtener FirebaseFirestore
    fun getFirestore(): FirebaseFirestore {
        return firestore
    }

    // Función para hacer login con correo y contraseña
    suspend fun login(email: String, password: String): AuthResult? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            null
        }
    }


    // Función para registrar un usuario con datos completos en Firestore
    suspend fun register(user: UserEntity): Boolean = try {
        val authResult = auth.createUserWithEmailAndPassword(user.email, user.password).await()
        authResult.user?.let { firebaseUser ->
            val userData = hashMapOf(
                "id" to user.id,
                "email" to user.email,
                "name" to user.name,
                "surname" to user.surname,
                "age" to user.age,
                "documentId" to user.documentId,
                "gender" to user.gender,
                "fiscalAddress" to user.fiscalAddress,
                "createdAt" to FieldValue.serverTimestamp(),
                "userType" to user.userType.name
            )
            firestore.collection("users").document(firebaseUser.uid).set(userData).await()

        }
        true
    } catch (e: Exception) {
        false
    }

    // Función para cerrar sesión
    fun logout() {
        auth.signOut()
    }

    // Obtener el usuario actual autenticado
    fun getCurrentUser() = auth.currentUser
}
