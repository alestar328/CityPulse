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
            // Intentamos obtener los métodos de inicio de sesión asociados al correo
            val result = FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email).await()

            // Si result.signInMethods no es nulo y tiene elementos, significa que el correo ya está registrado
            result.signInMethods?.isNotEmpty() == true
        } catch (e: Exception) {
            // Si ocurre un error (por ejemplo, si el correo no existe en Firebase), lo manejamos aquí
            Log.e("AuthCheck", "Error al verificar el correo: ${e.message}")
            false // Devolvemos false si hubo un error
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


    // Obtener el usuario actual autenticado
    fun getCurrentUser() = auth.currentUser
}
