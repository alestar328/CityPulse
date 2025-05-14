package com.app.citypulse.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.data.repository.EventRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

sealed class EventDetailsUiState {
    object Loading : EventDetailsUiState()
    data class Success(val event: EventUiModel, val isCreator: Boolean) : EventDetailsUiState()
    object Error : EventDetailsUiState() // O podrías incluir un mensaje de error
    object NotFound : EventDetailsUiState() // Para cuando el evento no existe
}

class EventViewModel : ViewModel() {
    private val repository = EventRepository()

    // Estado para la LISTA de eventos (para una pantalla de lista, si existe)
    private val _eventUiList = MutableStateFlow<List<EventUiModel>>(emptyList())
    val eventUiList: StateFlow<List<EventUiModel>> = _eventUiList.asStateFlow()

    // Estado para los DETALLES de un SOLO evento
    private val _eventDetailsUiState = MutableStateFlow<EventDetailsUiState>(EventDetailsUiState.Loading)
    val eventDetailsUiState: StateFlow<EventDetailsUiState> = _eventDetailsUiState.asStateFlow() // Exponer como StateFlow de solo lectura

    init {
        // Si la lista de eventos se carga al inicio para otra pantalla, mantén esto
        loadEvents()
    }


    private fun mapToUiModel(event: EventEntity): EventUiModel {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return EventUiModel(
            id = event.id,
            nombre = event.nombre,
            categoria = event.categoria,
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
            idRealizador = event.idRealizador,
            galleryPictureUrls = event.galleryPictureUrls ?: emptyList()
        )
    }

    fun createEvent(event: EventEntity, images: List<Uri>, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val eventId = repository.addEvent(event)
                println("📌 Evento creado con ID: $eventId")

                val imageUrls = if (images.isNotEmpty()) {
                    repository.uploadEventImages(eventId, images)
                } else {
                    emptyList()
                }

                if (imageUrls.isNotEmpty()) {
                    val updatedEvent = event.copy(galleryPictureUrls = imageUrls.toMutableList())
                    repository.updateEvent(updatedEvent)
                }

                onComplete(eventId)
            } catch (e: Exception) {
                println("⚠️ Error creando evento: ${e.message}")
                // Considerar emitir un estado de error o usar un canal de eventos para errores
            }
        }
    }


    fun deleteEvent(eventId: String, navController: NavController) {
        viewModelScope.launch {
            try {
                repository.deleteEvent(eventId)
                navController.popBackStack() // Navegación en el ViewModel (mejorable)
                println("✅ Evento $eventId eliminado.")
            } catch (e: Exception) {
                println("⚠️ Error al eliminar evento: ${e.message}")
                // Considerar emitir un estado de error
            }
        }
    }

    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                repository.updateEvent(event)
                // Después de actualizar, podrías querer recargar los detalles
                getEventById(event.id) // Recargar el evento específico
                // O recargar la lista si fuera necesario para otra pantalla
                // loadEvents()
                println("✅ Evento ${event.id} actualizado.")
            } catch (e: Exception) {
                println("⚠️ Error al actualizar evento: ${e.message}")
                // Considerar emitir un estado de error
            }
        }
    }

    private val _eventFlow = MutableStateFlow<EventEntity?>(null)
    val eventFlow: StateFlow<EventEntity?> = _eventFlow // Exponer correctamente

    fun getEventById(eventId: String) {
        viewModelScope.launch {
            _eventDetailsUiState.value = EventDetailsUiState.Loading // Indica carga al inicio
            try {
                val event = repository.getEventById(eventId)
                if (event != null) {
                    // Aquí se determina si es el creador y se crea el estado Success
                    val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                    val isCreator = currentUser != null && currentUser == event.idRealizador

                    // Mapeamos EventEntity a EventUiModel Y creamos el estado Success
                    val eventUiModel = mapToUiModel(event) // Usamos el mapeo original para obtener el UiModel
                    _eventDetailsUiState.value = EventDetailsUiState.Success(eventUiModel, isCreator)

                    println("🔹 Evento encontrado: ${event.nombre} con imágenes: ${event.galleryPictureUrls}")
                } else {
                    _eventDetailsUiState.value = EventDetailsUiState.NotFound // Indica que no se encontró
                    println("⚠️ No se encontró el evento con ID: $eventId")
                }
            } catch (e: Exception) {
                _eventDetailsUiState.value = EventDetailsUiState.Error // Indica error
                println("⚠️ Error al obtener evento: ${e.message}")
            }
        }
    }

    fun loadEvents() {
        // Podrías tener un estado de carga para la lista si la UI lo necesita
        // _uiState.value = UiState.Loading // Si usas UiState para la lista
        repository.getEvents { events ->
            // Mapear la lista de EventEntity a una lista de EventUiModel
            val uiEvents = events.map { mapToUiModel(it) }
            _eventUiList.value = uiEvents // Actualizar el StateFlow de la LISTA

            // Si tuvieras un estado UiState para la lista, lo actualizarías aquí:
            // _uiState.value = UiState.Success(uiEvents)
        }
    }

}