package com.app.citypulse.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Función para hacer login con correo y contraseña
    suspend fun login(email: String, password: String): AuthResult? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun checkIfUserExists(email: String): Boolean {
         try {
            // Referencia a la colección de 'users' en Firestore
            val db = FirebaseFirestore.getInstance()
            val usersCollection = db.collection("users")

            // Realizar una búsqueda en la colección 'users' donde el campo 'email' coincida con el correo dado
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()

            // Si la query devuelve algún documento, significa que el correo existe
            if (!querySnapshot.isEmpty) {
                Log.d("FirestoreCheck", "Correo $email encontrado en la colección de usuarios.")
                return true
            } else {
                Log.d("FirestoreCheck", "Correo $email no encontrado en la colección de usuarios.")
                return false
            }
        } catch (e: Exception) {
            Log.e("FirestoreCheck", "Error al verificar el correo en Firestore: ${e.message}")
            return false
        }
    }

    // Función para registrar un usuario con datos completos en Firestore
    suspend fun registerCompleteUser(
        email: String,
        password: String,  // Recibimos el password
        name: String,
        surname: String,
        documentId: String,
        gender: String,
        fiscalAddress: String?,
        userType: String,
        uid: String,
        google: String,
        friends: MutableList<String>
    ): Boolean {
        return try {
            // Primero, creamos al usuario con email y contraseña
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

            // Después de que el usuario sea creado exitosamente, guardamos los datos adicionales en Firestore
            val userData = hashMapOf(
                "name" to name,
                "surname" to surname,
                "documentId" to documentId,
                "gender" to gender,
                "fiscalAddress" to fiscalAddress.orEmpty(),
                "userType" to userType,
                "email" to email,
                "uid" to uid,
                "google" to google,
                "friends" to friends
            )

            // Guardamos la información en la colección de "users" usando el UID del usuario
            authResult.user?.let {
                firestore.collection("users")
                    .document(it.uid)
                    .set(userData)
                    .await()
            }

            true // El registro fue exitoso
        } catch (e: Exception) {
            false // Ocurrió un error en el registro
        }
    }

    // Función para cerrar sesión
    fun logout() {
        auth.signOut()
    }
}