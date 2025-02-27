package com.app.citypulse.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FriendsViewModel(private val authViewModel: AuthViewModel) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    // Lista de amigos
    private val _friends = MutableStateFlow<List<String>>(emptyList()) // Inicializa vacía
    val friends: StateFlow<List<String>> = _friends // Exposición de la lista de amigos

    // Función para obtener la lista de amigos del usuario actual
    fun loadFriends() {
        viewModelScope.launch {
            try {
                val currentUserUid = authViewModel.getCurrentUserUid()

                if (currentUserUid.isNotEmpty()) {
                    // Recuperar la lista de amigos desde Firestore
                    val docSnapshot = firestore.collection("users")
                        .document(currentUserUid)
                        .get()
                        .await()

                    val friendsList = docSnapshot.get("friends") as? List<String> ?: emptyList()
                    _friends.value = friendsList
                }
            } catch (e: Exception) {
                Log.e("FriendsViewModel", "Error loading friends: $e")
            }
        }
    }

    // Función para agregar un amigo a la lista del usuario actual
    fun addFriend(friendUid: String) {
        viewModelScope.launch {
            try {
                val currentUserUid = authViewModel.getCurrentUserUid()

                if (currentUserUid.isNotEmpty()) {
                    // Obtener la lista actual de amigos
                    val updatedFriendsList = _friends.value.toMutableList().apply {
                        add(friendUid)
                    }

                    // Actualizar la lista de amigos en Firestore
                    firestore.collection("users")
                        .document(currentUserUid)
                        .update("friends", updatedFriendsList)
                        .await()

                    // Actualizamos la lista de amigos localmente
                    _friends.value = updatedFriendsList
                }
            } catch (e: Exception) {
                Log.e("FriendsViewModel", "Error adding friend: $e")
            }
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