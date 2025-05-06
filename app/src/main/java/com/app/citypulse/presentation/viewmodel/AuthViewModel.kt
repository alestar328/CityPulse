package com.app.citypulse.presentation.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.data.enums.AccountType
import com.app.citypulse.data.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private var tempPhotoUrls: MutableList<String> = mutableListOf()

    // Estado de autenticación
    private val _isAuthenticated = MutableStateFlow(false)  // Inicialmente no está autenticado
    val isAuthenticated: StateFlow<Boolean> get() = _isAuthenticated  // Exponemos el estado

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _currentUser = MutableStateFlow<UserItem?>(null)
    // Propiedad pública para exponer el usuario actual
    val currentUser: StateFlow<UserItem?> get() = _currentUser

    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType

    fun loadUserData(onResult: (UserItem?) -> Unit) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val docSnapshot = if (currentUser.providerData.any { it.providerId == "google.com" }) {
                        firestore.collection("users")
                            .document(currentUser.email!!)
                            .get()
                            .await()
                    } else {
                        firestore.collection("users")
                            .document(currentUser.uid)
                            .get()
                            .await()
                    }
                    val userData = docSnapshot.toObject(UserItem::class.java)
                    onResult(userData)
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error loading user data", e)
                onResult(null)
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            val isSuccessful = result != null
            onResult(isSuccessful)

            // Si el login es exitoso, actualizamos el estado
            _isAuthenticated.value = isSuccessful

            if (isSuccessful) {
                loadUserType(email)  // Cargar el tipo de usuario
            }
        }
    }

    private fun loadUserType(email: String) {
        viewModelScope.launch {
            try {
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                val userRef = firestore.collection("users").document(uid)
                userRef.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val type = document.getString("userType")
                        _userType.value = type
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun updateProfilePictureUrl(newUrl: String, onResult: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userRef = firestore.collection("users").document(currentUser.uid)
            userRef.update("profilePictureUrl", newUrl)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } else {
            onResult(false)
        }
    }
    fun addGalleryPictureUrl(newUrl: String, onResult: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userRef = firestore.collection("users").document(currentUser.uid)

            userRef.get().addOnSuccessListener { snapshot ->
                val userItem = snapshot.toObject(UserItem::class.java)
                if (userItem != null) {
                    val updatedList = userItem.galleryPictureUrls.toMutableList().apply {
                        add(newUrl)
                    }
                    userRef.update("galleryPictureUrls", updatedList)
                        .addOnSuccessListener { onResult(true) }
                        .addOnFailureListener { onResult(false) }
                } else {
                    onResult(false)
                }
            }.addOnFailureListener {
                onResult(false)
            }
        } else {
            onResult(false)
        }
    }


    // Variables temporales para almacenar datos antes del registro final
    private var tempEmail: String? = null
    private var tempPassword: String? = null
    private var tempName: String? = null
    private var tempSurname: String? = null
    private var tempAge: Int? = null
    private var tempDocumentId: String? = null
    private var tempGender: String? = null
    private var tempFiscalAddress: String? = null
    private var tempUserType: AccountType? = null  // Variable para almacenar el tipo de cuenta

    // Guardar los datos del usuario temporalmente
    fun setTempUserData(email: String, password: String) {
        tempEmail = email
        tempPassword = password
    }

    fun saveUser(user: UserItem, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Creamos un mapa con los datos del usuario
            val userData = hashMapOf(
                "name" to user.name,
                "surname" to user.surname,
                "email" to user.email,
                "age" to user.age,
                "documentId" to (user.documentId ?: ""),
                "gender" to (user.gender ?: ""),
                "userType" to (user.userType?.name ?: "Persona"),
                "valoracion" to (user.valoracion ?: 0),
                "password" to user.password,
                "google" to (user.google ?: "No"),
                "uid" to (user.uid ?: ""),
                "friends" to user.friends
            )


            // Referencia a la colección "users" en Firestore
            val userRef = firestore.collection("users").document(user.email) // Usamos el correo como ID del documento

            try {
                // Guardamos el usuario en Firestore
                userRef.set(userData).addOnSuccessListener {
                    onResult(true)  // Si se guardó correctamente
                }.addOnFailureListener { exception ->
                    onResult(false)  // Si hubo un error al guardar
                    println("Error saving user: $exception")
                }
            } catch (e: Exception) {
                onResult(false)
                println("Error saving user: $e")
            }
        }
    }

    // Función para registrar el usuario con los datos completos
    fun registerCompleteUser(
        userItem: UserItem,  // Aquí recibimos el objeto completo con todos los campos
        fiscalAddress: String?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            // Verificar si los datos temporales están completos antes de continuar
            if (tempEmail.isNullOrEmpty() || tempPassword.isNullOrEmpty()) {
                onResult(false)  // Si no hay email o contraseña, no continuamos
                return@launch
            }

            // Realiza el registro del usuario completo, incluyendo el tipo de cuenta
            val result = authRepository.registerCompleteUser(
                tempEmail ?: "",  // Usamos el email temporal guardado
                tempPassword ?: "",  //
                name = userItem.name,
                surname = userItem.surname,
                documentId = userItem.documentId ?: "",  // Si 'documentId' es null, se usa una cadena vacía ""
                gender = userItem.gender ?: "",  // Si 'gender' es null, se usa una cadena vacía ""
                fiscalAddress = fiscalAddress ?: "",  // Si 'fiscalAddress' es null, se usa una cadena vacía ""
                userType = userItem.userType.name,
                uid = userItem.uid ?: "",  // Si 'uid' es null, se usa una cadena vacía ""
                google = userItem.google ?: "no",  // Si 'google' es null, se usa "no"
                friends = userItem.friends.takeIf { it.isNotEmpty() } ?: mutableListOf()  // Si 'friends' es null o vacío, se usa una lista vacía
            )

            // Verificar si el registro fue exitoso
            val isSuccessful = result != null
            onResult(isSuccessful)

            // Si el registro es exitoso, actualizamos el estado de autenticación
            if (isSuccessful) {
                _isAuthenticated.value = true
            } else {
                _isAuthenticated.value = false
            }
        }
    }

    // Obtener los datos temporales del usuario
    fun getTempUserData(): Map<String, Any?> {
        return mapOf(
            "email" to tempEmail,
            "password" to tempPassword,
            "name" to tempName,
            "surname" to tempSurname,
            "age" to tempAge,
            "documentId" to tempDocumentId,
            "gender" to tempGender,
            "fiscalAddress" to tempFiscalAddress,
            "userType" to tempUserType,  // Agregar el tipo de cuenta
            "photoUrls" to tempPhotoUrls

        )
    }

    // Obtener el cliente de inicio de sesión de Google
    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("276829590899-b57qecnv0f414u2l3ss2kfgg6vdn99ik.apps.googleusercontent.com")
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    fun checkIfUserExists(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val exists = authRepository.checkIfUserExists(email)
                Log.d("AuthCheck", "Correo $email existe: $exists")
                onResult(exists)
            } catch (e: Exception) {
                Log.e("AuthCheck", "Error al verificar el correo: ${e.message}")
                onResult(false)  // En caso de error, devolvemos false
            }
        }
    }



    // Marca esta función como 'suspend' para permitir que use 'await'
    suspend fun getCurrentUser(): UserItem? {
        val currentUser = auth.currentUser
        return if (currentUser != null && currentUser.email != null) {
            try {
                // Obtenemos el documento de Firestore de manera suspendida
                val docSnapshot = firestore.collection("users")
                    .document(currentUser.email!!)  // Usamos el correo electrónico como el ID del documento
                    .get()
                    .await()  // Ahora está dentro de una función suspensiva

                // Convertimos el documento a un objeto UserItem
                docSnapshot.toObject(UserItem::class.java)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error loading user data", e)
                null
            }
        } else {
            null // Si no hay usuario autenticado, devolvemos null
        }
    }

    fun checkifGoogleUserExists(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val docSnapshot = firestore.collection("users")
                    .document(email) // Usamos el correo como ID del documento
                    .get()
                    .await()

                onResult(docSnapshot.exists())
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error checking user existence", e)
                onResult(false)
            }
        }
    }



    fun logout(onLogoutComplete: () -> Unit) {
        // Lógica para cerrar la sesión (por ejemplo, usando Firebase Auth)
        Firebase.auth.signOut()

        // Limpiar cualquier dato de usuario en el ViewModel
        clearUserData()

        // Llamar al callback para notificar que el logout ha sido completado
        onLogoutComplete()
    }

    private fun clearUserData() {
        // Limpiar cualquier dato de usuario en el ViewModel
        // Por ejemplo, establecer el usuario actual a null
        _currentUser.value = null
    }
}

