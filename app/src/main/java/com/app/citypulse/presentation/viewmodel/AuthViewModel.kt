package com.app.citypulse.presentation.viewmodel

import android.accounts.Account
import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.dataUsers.AccountType
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.data.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()



    // Estado de autenticación
    private val _isAuthenticated = MutableStateFlow(false)  // Inicialmente no está autenticado
    val isAuthenticated: StateFlow<Boolean> get() = _isAuthenticated  // Exponemos el estado
    

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun loadUserData(onResult: (UserItem?) -> Unit) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser != null && currentUser.email != null) {
                    // Se asume que en Firestore se guarda el usuario con el ID igual a su email.
                    val docSnapshot = firestore.collection("users")
                        .document(currentUser.email!!)
                        .get()
                        .await()
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
        }
    }

    fun register(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.register(email, password)
            val isSuccessful = result != null
            onResult(isSuccessful)

            // Si el registro es exitoso, actualizamos el estado
            _isAuthenticated.value = isSuccessful
        }
    }

    fun logout() {
        authRepository.logout()
        _isAuthenticated.value =
            false  // Actualizamos el estado cuando el usuario cierra sesión
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
                "documentId" to user.documentId,
                "gender" to user.gender,
                "userType" to (user.userType?.name ?: "Persona")  // Si userType es null, usamos "PERSON" como valor por defecto
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
            "userType" to tempUserType  // Agregar el tipo de cuenta
        )
    }

    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("79725582630-2mn226j2pnrpcn7ljg07j6o7hish6p3s.apps.googleusercontent.com")
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

    fun getCurrentUserUid(): String {
        return auth.currentUser?.uid ?: "" // Retorna el UID o una cadena vacía si no hay usuario
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


    fun signInWithGoogle(account: GoogleSignInAccount, onResult: (Boolean) -> Unit) {
        val idToken = account.idToken
        if (idToken != null) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            viewModelScope.launch {
                try {
                    val authResult: AuthResult = auth.signInWithCredential(credential).await()

                    val user = auth.currentUser
                    if (user != null) {
                        val userData = UserItem(
                            name = user.displayName ?: "",
                            surname = "",
                            age = 0,
                            email = user.email ?: "",
                            documentId = null,
                            userType = AccountType.Persona,
                            valoracion = null,
                            password = "",
                            gender = null,
                            uid = user.uid,
                            friends = mutableListOf()
                        )

                        firestore.collection("users").document(user.uid).set(userData).await()

                        // 🔹 Guardamos el usuario en FirebaseAuth para que mantenga la sesión
                        _isAuthenticated.value = true
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Error al autenticar usuario con Google", e)
                    onResult(false)
                }
            }
        } else {
            onResult(false)
        }
    }

    fun checkUserSession(onResult: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        onResult(currentUser != null)
    }


}

