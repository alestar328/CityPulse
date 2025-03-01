package com.app.citypulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.data.repository.UserRepository
import kotlinx.coroutines.launch

class PersonViewModel : ViewModel() {

    private val repository = UserRepository()

    // 🔹 Agregar una persona a Firestore
    fun addPerson(user: UserItem) {
        viewModelScope.launch {
            try {
                repository.addUser(user)
                println(" Persona añadida correctamente")
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
                    println("Persona: ${it.name} ${it.surname}, Edad: ${it.age}")
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
                println(" Persona actualizada correctamente")
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
                println("Persona eliminada correctamente")
            } catch (e: Exception) {
                println("Error al eliminar persona: ${e.message}")
            }
        }
    }
}