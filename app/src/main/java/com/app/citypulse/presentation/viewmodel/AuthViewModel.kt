package com.app.citypulse.presentation.viewmodel

import android.app.Activity
import android.net.Uri
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private var tempPhotoUrls: MutableList<String> = mutableListOf()
    private val auth = FirebaseAuth.getInstance()
    val storage = Firebase.storage
    var storageRef = storage.reference
    // Estado de autenticación
    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    val isAuthenticated: StateFlow<Boolean> get() = _isAuthenticated  // Exponemos el estado

    private val firestore = FirebaseFirestore.getInstance()

    private val _currentUser = MutableStateFlow<UserItem?>(null)
    // Propiedad pública para exponer el usuario actual
    val currentUser: StateFlow<UserItem?> = _currentUser

    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType

    private val _galleryUrls = MutableStateFlow<List<String>>(emptyList())
    val galleryUrls: StateFlow<List<String>> = _galleryUrls


    init{
        auth.addAuthStateListener { firebaseAuth ->
            _isAuthenticated.value = firebaseAuth.currentUser != null
            if(_isAuthenticated.value)loadUserData()
        }
    }
    private suspend fun uploadToStorage(path: String, uri: Uri): String =
        storage.reference
            .child(path)
            .putFile(uri)
            .await()                            // de kotlinx-coroutines-play-services
            .storage
            .downloadUrl
            .await()
            .toString()


    fun loadUserData() {
        viewModelScope.launch {
            val firebaseUser = auth.currentUser
            if (firebaseUser == null) {
                _currentUser.value = null
                return@launch
            }
            // always use UID
            val doc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            _currentUser.value = if (doc.exists()) doc.toObject(UserItem::class.java) else null
            loadGallery()
        }
    }
    fun loadGallery() = viewModelScope.launch {
        val uid = auth.currentUser?.uid ?: return@launch
        val snaps = firestore
            .collection("users")
            .document(uid)
            .collection("gallery")
            .orderBy("createdAt")
            .get()
            .await()
        _galleryUrls.value = snaps.mapNotNull { it.getString("url") }
    }
    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            val isSuccessful = result != null
            onResult(isSuccessful)

            // Si el login es exitoso, actualizamos el estado
            _isAuthenticated.value = isSuccessful

            if (isSuccessful) {
                loadUserType()  // Cargar el tipo de usuario
            }
        }
    }

    fun loadUserType() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val doc = firestore
                    .collection("users")
                    .document(uid)
                    .get()
                    .await()
                _userType.value = doc.getString("userType")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Couldn't load userType", e)
            }
        }
    }

    fun uploadProfileImage(uri: Uri, onResult: (Boolean, String?)->Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(false, null)
        viewModelScope.launch {
            runCatching {
                // 1) Sube al storage y obtiene URL de descarga
                val path = "users/$uid/profile.jpg"
                val downloadUrl = uploadToStorage(path, uri)

                // 2) Actualiza el campo profilePictureUrl en Firestore
                firestore.collection("users")
                    .document(uid)
                    .update("profilePictureUrl", downloadUrl)
                    .await()

                // 3) Actualiza tu StateFlow para que Compose re-reemplace la imagen
                _currentUser.value = _currentUser.value
                    ?.copy(profilePictureUrl = downloadUrl)

                onResult(true, downloadUrl)
            }.onFailure { throwable ->
                Log.e("AuthVM", "uploadProfileImage failed", throwable)
                onResult(false, null)
            }
        }
    }



    fun uploadGalleryImage(uri: Uri, onResult: (Boolean, String?)->Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(false, null)
        viewModelScope.launch {
            runCatching {
                // 1) Sube al storage y obtiene URL
                val filename = "gallery/photo_${System.currentTimeMillis()}.jpg"
                val downloadUrl = uploadToStorage("users/$uid/$filename", uri)

                // 2) Crea un documento en la sub-colección `gallery`
                val colRef = firestore
                    .collection("users")
                    .document(uid)
                    .collection("gallery")

                colRef.add(mapOf(
                    "url" to downloadUrl,
                    "createdAt" to FieldValue.serverTimestamp()
                )).await()

                // 3) ¡PRUNE! Borramos todo lo que exceda de 3
                val allSnaps = colRef
                    .orderBy("createdAt", Query.Direction.ASCENDING)
                    .get()
                    .await()

                if (allSnaps.size() > 3) {
                    val toDelete = allSnaps.documents.take(allSnaps.size() - 3)
                    toDelete.forEach { doc ->
                        colRef.document(doc.id).delete()
                    }
                }

                // 4) Notifica el callback y recarga galería
                onResult(true, downloadUrl)
                loadGallery()
            }.onFailure {
                Log.e("AuthVM", "uploadGalleryImage", it)
                onResult(false, null)
            }
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
            val currentUid = FirebaseAuth.getInstance().currentUser?.uid
            if (currentUid == null) {
                onResult(false)
                return@launch
            }

            // Creamos un mapa con los datos del usuario
            val userData = hashMapOf(
                "name" to user.name,
                "surname" to user.surname,
                "email" to user.email,
                "age" to user.age,
                "documentId" to (user.documentId ?: ""),
                "gender" to (user.gender ?: ""),
                "userType" to (user.userType?.name ?: "PERSON"),
                "valoracion" to (user.valoracion ?: 0),
                "password" to user.password,
                "google" to (user.google ?: "No"),
                "uid" to (user.uid ?: ""),
                "friends" to user.friends
            )


            // Referencia a la colección "users" en Firestore
            val userRef = firestore.collection("users").document(currentUid)  // Usamos el correo como ID del documento

            try {
                // Guardamos el usuario en Firestore
                userRef.set(userData, SetOptions.merge()).addOnSuccessListener {
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
                _userType.value = userItem.userType?.name
                loadUserType()
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
        Firebase.auth.signOut()
        clearUserData()
        onLogoutComplete()
    }

    private fun clearUserData() {
        // Limpiar cualquier dato de usuario en el ViewModel
        // Por ejemplo, establecer el usuario actual a null
        _currentUser.value = null
    }
}

