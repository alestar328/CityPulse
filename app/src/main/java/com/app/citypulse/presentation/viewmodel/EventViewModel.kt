package com.app.citypulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.data.repository.EventRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Locale

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
}

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventUiList = MutableStateFlow<List<EventUiModel>>(emptyList())
    val eventUiList: StateFlow<List<EventUiModel>> = _eventUiList

    private val _uiState = MutableStateFlow<UiState<List<EventUiModel>>>(UiState.Loading)

    init {
        loadEvents()
    }

    private fun mapToUiModel(event: EventEntity): EventUiModel {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return EventUiModel(
            id = event.id,
            nombre = event.nombre,
            categoria = event.categoria.name,
            subcategoria = event.subcategoria,
            descripcion = event.descripcion,
            lugar = event.lugar,
            latitud = event.latitud,
            longitud = event.longitud,
            fechaInicio = event.fechaInicio?.let { dateFormat.format(it) } ?: "Sin fecha",
            fechaFin = event.fechaFin?.let { dateFormat.format(it) } ?: "Sin fecha",
            aforo = event.aforo,
            precio = event.precio,
            valoracion = event.valoracion,
            idRealizador = event.idRealizador
        )
    }

    fun createEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                repository.addEvent(event)
                // No es necesario llamar a loadEvents() porque Firestore debería actualizarse solo.
            } catch (e: Exception) {
                println("Error al agregar evento: ${e.message}")
            }
        }
    }

    fun deleteEvent(eventId: String, navController: NavController) {
        viewModelScope.launch {
            try {
                repository.deleteEvent(eventId)
                navController.popBackStack()
            } catch (e: Exception) {
                println("Error al eliminar evento: ${e.message}")
            }
        }
    }

    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                repository.updateEvent(event)
                loadEvents()
            } catch (e: Exception) {
                println("Error al actualizar evento: ${e.message}")
            }
        }
    }


    private val _eventFlow = MutableStateFlow<EventEntity?>(null)
    val eventFlow: StateFlow<EventEntity?> = _eventFlow // Exponer correctamente

    fun getEventById(eventId: String) {
        viewModelScope.launch {
            val event = repository.getEventById(eventId)
            _eventFlow.value = event
        }
    }

    fun loadEvents() {
        _uiState.value = UiState.Loading
        repository.getEvents { events ->
            val uiEvents = events.map { mapToUiModel(it) }
            _eventUiList.value = uiEvents
            _uiState.value = UiState.Success(uiEvents)
        }
    }
}