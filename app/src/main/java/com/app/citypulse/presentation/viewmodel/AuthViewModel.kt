package com.app.citypulse.presentation.viewmodel

import android.accounts.Account
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.dataUsers.AccountType
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    // Obteniendo auth y firestore desde AuthRepository
    private val auth: FirebaseAuth = authRepository.getFirebaseAuth()
    private val firestore: FirebaseFirestore = authRepository.getFirestore()

    // Estado de autenticación
    private val _isAuthenticated = MutableStateFlow(false)  // Inicialmente no está autenticado
    val isAuthenticated: StateFlow<Boolean> get() = _isAuthenticated  // Exponemos el estado

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
        _isAuthenticated.value = false  // Actualizamos el estado cuando el usuario cierra sesión
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

    // Guardar los datos adicionales del usuario para completar el perfil
    fun completeTempUserData(
        name: String,
        surname: String,
        age: Int,
        documentId: String,
        gender: String,
        fiscalAddress: String?,
        userType: AccountType  // Agregar el parámetro para el tipo de cuenta
    ) {
        tempName = name
        tempSurname = surname
        tempAge = age
        tempDocumentId = documentId
        tempGender = gender
        tempFiscalAddress = fiscalAddress ?: ""  // Valor por defecto si es null
        tempUserType = userType  // Guardamos el tipo de cuenta
    }

    // Función para registrar el usuario con los datos completos
    fun registerCompleteUser(
        name: String,
        surname: String,
        age: Int,
        documentId: String,
        gender: String,
        fiscalAddress: String?,
        userType: AccountType,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            // Realiza el registro del usuario completo, incluyendo el tipo de cuenta
            val result = authRepository.registerCompleteUser(
                tempEmail ?: "",  // Usamos el email temporal guardado
                tempPassword ?: "",  // Usamos la contraseña temporal guardada
                name, surname, age, documentId, gender, fiscalAddress,
                userType.name  // Pasamos el tipo de cuenta
            )

            val isSuccessful = result != null
            onResult(isSuccessful)

            // Si el registro es exitoso, actualizamos el estado
            _isAuthenticated.value = isSuccessful
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

    // Limpiar los datos temporales después de un intento de registro o inicio de sesión
    fun clearTempUserData() {
        tempEmail = null
        tempPassword = null
        tempName = null
        tempSurname = null
        tempAge = null
        tempDocumentId = null
        tempGender = null
        tempFiscalAddress = null
        tempUserType = null  // Limpiar el tipo de cuenta
    }
    fun getUserData(onResult: (UserItem?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onResult(null)

        viewModelScope.launch {
            try {
                val document = firestore.collection("users").document(userId).get().await()
                val user = document.toObject(UserItem::class.java)
                onResult(user)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }
}
