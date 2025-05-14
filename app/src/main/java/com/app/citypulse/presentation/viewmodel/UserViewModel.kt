package com.app.citypulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()
    private val _savedEvents = MutableStateFlow<List<String>>(emptyList())
    val savedEvents: StateFlow<List<String>> = _savedEvents.asStateFlow()

    fun saveEventForUser(eventId: String) = viewModelScope.launch {
        repository.saveEventForUser(eventId)
        _savedEvents.value = repository.getSavedEventIdsForUser()
    }
    init {
        viewModelScope.launch {
            _savedEvents.value = repository.getSavedEventIdsForUser()
        }
    }

    // 🔹 Agregar una persona a Firestore
    fun addPerson(user: UserItem) {
        viewModelScope.launch {
            try {
                repository.addUser(user)
                println(" PERSON añadida correctamente")
            } catch (e: Exception) {
                println(" Error al añadir persona: ${e.message}")
            }
        }
    }

    // 🔹 Obtener todas las personas
    fun fetchPersons() {
        viewModelScope.launch {
            try {
                val persons = repository.getUsers()
                persons.forEach {
                    println("PERSON: ${it.name} ${it.surname}, Edad: ${it.age}")
                }
            } catch (e: Exception) {
                println(" Error al obtener personas: ${e.message}")
            }
        }
    }

    // 🔹 Actualizar una persona en Firestore
    fun updatePerson(id: String, newUser: UserItem) {
        viewModelScope.launch {
            try {
                repository.updateUser(id, newUser)
                println(" PERSON actualizada correctamente")
            } catch (e: Exception) {
                println(" Error al actualizar persona: ${e.message}")
            }
        }
    }

    // 🔹 Eliminar una persona de Firestore
    fun deletePerson(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteUser(id)
                println("PERSON eliminada correctamente")
            } catch (e: Exception) {
                println("Error al eliminar persona: ${e.message}")
            }
        }
    }
}