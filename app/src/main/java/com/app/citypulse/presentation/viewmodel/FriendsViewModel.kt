package com.app.citypulse.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FriendsViewModel(private val authViewModel: AuthViewModel) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    val auth = Firebase.auth // Obtén la instancia de Firebase Auth

    // Lista de amigos
    private val _friends = MutableStateFlow<List<String>>(emptyList()) // Inicializa vacía
    val friends: StateFlow<List<String>> = _friends // Exposición de la lista de amigos
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val uid = firebaseUser?.uid  // El UID de Firebase
    val email = firebaseUser?.email  // El email de Google

    // Función para obtener la lista de amigos del usuario actual
    fun loadFriends() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    // Verificar si el usuario se autenticó con Google
                    val isGoogleUser =
                        currentUser.providerData.any { it.providerId == "google.com" }

                    // Usar el email si es un usuario de Google, de lo contrario usar el UID
                    val userIdentifier = if (isGoogleUser) {
                        currentUser.email ?: run {
                            Log.e("FriendsViewModel", "Google user email is null!")
                            return@launch
                        }
                    } else {
                        currentUser.uid
                    }

                    Log.d(
                        "FriendsViewModel",
                        "User identifier: $userIdentifier"
                    ) // Verificar el identificador

                    // Recuperar la lista de amigos desde Firestore
                    val docSnapshot = firestore.collection("users")
                        .document(userIdentifier)
                        .get()
                        .await()

                    Log.d(
                        "FriendsViewModel",
                        "Document snapshot exists: ${docSnapshot.exists()}"
                    ) // Verificar si el documento existe

                    if (docSnapshot.exists()) {
                        // Obtener la lista de amigos de Firestore
                        val friendsList = docSnapshot.get("friends") as? List<String> ?: emptyList()

                        // Log para verificar qué datos estamos obteniendo
                        Log.d("FriendsViewModel", "Retrieved friends: $friendsList")

                        // Actualizar la lista de amigos en el modelo
                        _friends.value = friendsList.toMutableList()

                        // Log para confirmar que se ha actualizado la lista de amigos
                        Log.d("FriendsViewModel", "Updated _friends: ${_friends.value}")
                    } else {
                        Log.e(
                            "FriendsViewModel",
                            "Document does not exist for identifier: $userIdentifier"
                        )
                        _friends.value =
                            emptyList() // Asegúrate de que la lista esté vacía si no hay documento
                    }
                } else {
                    Log.e("FriendsViewModel", "Current user is null!")
                    _friends.value =
                        emptyList() // Asegúrate de que la lista esté vacía si no hay usuario autenticado
                }
            } catch (e: Exception) {
                Log.e("FriendsViewModel", "Error loading friends: $e")
            }
        }
    }

    fun addFriend(
        friendUid: String,
        friendsList: List<String>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onError("No se pudo obtener el usuario actual")
            return
        }

        // Verificar si el usuario se autenticó con Google
        val isGoogleUser = currentUser.providerData.any { it.providerId == "google.com" }

        // Usar el email si es un usuario de Google, de lo contrario usar el UID
        val currentUserUid = if (isGoogleUser) {
            currentUser.email ?: run {
                onError("Email del usuario de Google no encontrado")
                return
            }
        } else {
            currentUser.uid
        }

        if (friendUid.isBlank()) {
            onError("El UID no puede estar vacío")
            return
        }

        if (friendUid == currentUserUid) {
            onError("No puedes agregarte a ti mismo")
            return
        }

        if (friendsList.contains(friendUid)) {
            onError("Este amigo ya está en tu lista")
            return
        }

        viewModelScope.launch {
            try {
                // Verificar si la UID del amigo existe en la base de datos
                val friendRef = firestore.collection("users").document(friendUid)
                val friendDocument = friendRef.get().await()

                if (!friendDocument.exists()) {
                    onError("El usuario con UID $friendUid no existe")
                    return@launch
                }

                // Si la UID existe, procedemos a agregarla a la lista de amigos
                val userRef = firestore.collection("users").document(currentUserUid)
                val updatedFriendsList = friendsList.toMutableList().apply { add(friendUid) }

                userRef.set(mapOf("friends" to updatedFriendsList), SetOptions.merge()).await()

                _friends.value = updatedFriendsList
                onSuccess()
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error adding friend: $e")
                onError("Hubo un error al agregar el amigo")
            }
        }
    }



    fun removeFriend(
        friendUid: String,
        currentUser: FirebaseUser?, // Pasamos el FirebaseUser en lugar del UID
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Verificar si el usuario actual es nulo
                if (currentUser == null) {
                    onError("No se pudo obtener el usuario actual")
                    return@launch
                }

                // Determinar si el usuario se autenticó con Google
                val isGoogleUser =
                    currentUser.providerData.any { it.providerId == "google.com" }

                // Usar el email si es un usuario de Google, de lo contrario usar el UID
                val userIdentifier = if (isGoogleUser) {
                    currentUser.email ?: run {
                        onError("El usuario de Google no tiene un correo válido")
                        return@launch
                    }
                } else {
                    currentUser.uid
                }

                // Verificar si el UID del amigo existe en la lista de amigos
                if (!_friends.value.contains(friendUid)) {
                    onError("El usuario con UID $friendUid no está en tu lista de amigos")
                    return@launch
                }

                // Eliminar el amigo de la lista del usuario actual
                val updatedFriendsList =
                    _friends.value.toMutableList().apply { remove(friendUid) }

                // Actualizar Firestore
                val userRef = firestore.collection("users").document(userIdentifier)
                userRef.update("friends", updatedFriendsList).await()

                // Actualizar el estado local
                _friends.value = updatedFriendsList

                // Notificar éxito
                onSuccess()
            } catch (e: Exception) {
                onError("Hubo un error al eliminar el amigo: ${e.message}")
            }
        }
    }

    // FriendsViewModel.kt
    fun getFriendName(friendId: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                // Buscar el documento del amigo en Firestore
                val friendDoc = firestore.collection("users").document(friendId).get().await()
                if (friendDoc.exists()) {
                    // Extraer el campo "name"
                    val name = friendDoc.getString("name")
                    onResult(name)
                } else {
                    onResult(null) // El documento no existe
                }
            } catch (e: Exception) {
                Log.e("FriendsViewModel", "Error al obtener el nombre del amigo", e)
                onResult(null)
            }
        }
    }


    class FriendsViewModelFactory(
        private val authViewModel: AuthViewModel
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
                return FriendsViewModel(authViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}