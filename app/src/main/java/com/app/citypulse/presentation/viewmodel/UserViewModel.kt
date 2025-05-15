package com.app.citypulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class UserViewModel(
    private val userRepo: UserRepository = UserRepository(),
    private val eventRepo: EventRepository = EventRepository()
) : ViewModel() {
    private val repository = UserRepository()
    private val _savedEvents = MutableStateFlow<List<EventUiModel>>(emptyList())
    val savedEvents: StateFlow<List<EventUiModel>> = _savedEvents.asStateFlow()

    fun saveEventForUser(eventId: String) = viewModelScope.launch {
        userRepo.saveEventForUser(eventId)
        loadSavedEvents() // recarga tras guardar
    }
    init {
        loadSavedEvents()
    }
    private fun loadSavedEvents() = viewModelScope.launch {
        // 1) IDs guardados
        val ids = userRepo.getSavedEventIdsForUser()
        // 2) por cada ID, recuperar entidad y mapear a UI
        val models = ids.mapNotNull { id ->
            eventRepo.getEventById(id)?.let { entity ->
                // aquí copias tu mapToUiModel
                val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                EventUiModel(
                    id               = entity.id,
                    nombre           = entity.nombre,
                    nomOrg           = entity.nombreOrg,
                    categoria        = entity.categoria,
                    subcategoria     = entity.subcategoria,
                    descripcion      = entity.descripcion,
                    lugar            = entity.lugar,
                    latitud          = entity.latitud,
                    longitud         = entity.longitud,
                    fechaInicio      = entity.fechaInicio?.let { dateFormat.format(it) } ?: "Sin fecha",
                    fechaFin         = entity.fechaFin?.let { dateFormat.format(it) } ?: "Sin fecha",
                    aforo            = entity.aforo,
                    precio           = entity.precio,
                    valoracion       = entity.valoracion,
                    idRealizador     = entity.idRealizador,
                    galleryPictureUrls = entity.galleryPictureUrls ?: emptyList()
                )
            }
        }
        _savedEvents.value = models
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