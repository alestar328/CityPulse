package com.app.citypulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.citypulse.data.dataUsers.AccountType
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.data.model.UserEntity
import com.app.citypulse.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel(){
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated : StateFlow<Boolean> get() = _isAuthenticated
    private var tempUser: UserEntity? = null //Esto guarda en cahe los datos del usuario

    fun login(email: String, password: String, onResult:(Boolean)-> Unit){
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            val isSuccessful = result != null
            _isAuthenticated.value = isSuccessful
            onResult(isSuccessful)
        }
    }

    fun saveTempUserData(email: String, password: String){
        tempUser = UserEntity(
            id=0,
            email =email,
            password = password,
            userType = AccountType.Persona
        )
    }
    fun getTempUserData(): UserEntity? = tempUser

    fun registerUser(user: UserEntity, onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            val isSuccessful = authRepository.register(user)
            _isAuthenticated.value = isSuccessful
            onResult(isSuccessful)
        }
    }
    fun logout(){
        authRepository.logout()
        _isAuthenticated.value = false
    }
    fun getCurrentUserFromFirestore(): UserItem? {
        var userItem: UserItem? = null
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            currentUser?.let { firebaseUser ->
                val snapshot = authRepository.getFirestore().collection("users").document(firebaseUser.uid).get().await()
                userItem = snapshot.toObject(UserItem::class.java)
            }
        }
        return userItem
    }
}