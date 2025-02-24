package com.app.citypulse.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
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

    // Función para registrar un usuario con correo y contraseña
    suspend fun register(email: String, password: String): AuthResult? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun checkIfUserExists(email: String): Boolean {
        return try {
            // Intentamos registrar al usuario con el correo y una contraseña ficticia
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, "dummyPassword123").await()

            // Si no ocurre ninguna excepción, significa que el correo no está registrado
            FirebaseAuth.getInstance().signOut()  // Desconectamos al usuario creado
            false // El correo no está registrado
        } catch (e: FirebaseAuthException) {
            // Si ocurre una excepción, significa que el correo ya está registrado
            true // El correo ya está registrado
        } catch (e: Exception) {
            // Manejar cualquier otro tipo de error
            Log.e("AuthCheck", "Error al verificar el correo: ${e.message}")
            false // Devolvemos false si hay un error
        }
    }

    // Función para registrar un usuario con datos completos en Firestore
    suspend fun registerCompleteUser(
        email: String,
        password: String,
        name: String,
        surname: String,
        age: Int,
        documentId: String,
        gender: String,
        fiscalAddress: String?,
        userType: String
    ): Boolean {
        return try {
            // Primero, creamos al usuario con email y contraseña
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

            // Después de que el usuario sea creado exitosamente, guardamos los datos adicionales en Firestore
            val userData = hashMapOf(
                "name" to name,
                "surname" to surname,
                "age" to age,
                "documentId" to documentId,
                "gender" to gender,
                "fiscalAddress" to fiscalAddress.orEmpty(),
                "UserType" to userType,
                "email" to email // Guardar el email también en Firestore
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
