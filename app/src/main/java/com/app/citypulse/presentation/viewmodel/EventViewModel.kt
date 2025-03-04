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
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
}

class EventViewModel : ViewModel() {
    private val repository = EventRepository()
    private val _eventUiList = MutableStateFlow<List<EventUiModel>>(emptyList())
    val eventUiList: StateFlow<List<EventUiModel>> = _eventUiList

    private val _uiState = MutableStateFlow<UiState<List<EventUiModel>>>(UiState.Loading)
    private val _uploadState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val uploadState: StateFlow<UiState<List<String>>> = _uploadState
    init {
        loadEvents()
    }

    private fun mapToUiModel(event: EventEntity): EventUiModel {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return EventUiModel(
            id = event.id,
            nombre = event.nombre,
            categoria = event.categoria, // Se asigna el valor de event
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
            galleryPictureUrls = event.galleryPictureUrls // 🔹 Ahora cargamos la lista de imágenes

        )
    }

    fun createEvent(event: EventEntity, images: List<Uri>, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val eventId = repository.addEvent(event) // 🔹 Guardar evento y obtener su ID
                println("📌 Evento creado con ID: $eventId")

                if (images.isNotEmpty()) {
                    repository.uploadEventImages(eventId, images) { imageUrls ->
                        println("✅ Imágenes subidas correctamente: $imageUrls")

                        // 🔹 Asegurar que el evento también guarda las imágenes sin sobrescribirlas
                        event.galleryPictureUrls = (event.galleryPictureUrls + imageUrls).distinct().toMutableList()

                        onComplete(eventId) // 🔹 Ejecutamos el callback una vez que todo está listo
                    }
                } else {
                    onComplete(eventId) // 🔹 Si no hay imágenes, devolvemos el ID directamente
                }
            } catch (e: Exception) {
                println("⚠️ Error creando evento: ${e.message}")
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
            if (event != null) {
                println("🔹 Evento encontrado: ${event.nombre} con imágenes: ${event.galleryPictureUrls}")
            } else {
                println("⚠️ No se encontró el evento con ID: $eventId")
            }
            _eventFlow.value = event?.copy(
                galleryPictureUrls = event.galleryPictureUrls ?: mutableListOf()
            )
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



    fun uploadSelectedImagesToFirebase(
        uris: List<Uri>,
        eventId: String,
        onComplete: (List<String>) -> Unit
    ) {
        viewModelScope.launch {
            val storageRef = Firebase.storage.reference.child("event_images/$eventId")
            val imageUrls = mutableListOf<String>()

            try {
                withContext(Dispatchers.IO) {
                    uris.map { uri ->
                        async {
                            val fileName = "${System.currentTimeMillis()}.jpg"
                            val fileRef = storageRef.child(fileName)

                            fileRef.putFile(uri).await()
                            val downloadUri = fileRef.downloadUrl.await()
                            imageUrls.add(downloadUri.toString())
                        }
                    }.awaitAll()
                }

                _uploadState.value = UiState.Success(imageUrls)
                onComplete(imageUrls) // 🔹 Correcto: ahora devolvemos una lista de Strings
            } catch (e: Exception) {
                _uploadState.value = UiState.Loading
                println("Error al subir imagen: ${e.message}")
                onComplete(emptyList()) // 🔹 Evitamos un error si falla la subida
            }
        }
    }
    fun uploadEventImages(eventId: String, uris: List<Uri>, onComplete: (List<String>) -> Unit) {
        viewModelScope.launch {
            try {
                repository.uploadEventImages(eventId, uris, onComplete) // 🔹 Llamamos al repositorio correctamente
            } catch (e: Exception) {
                println("⚠️ Error al subir imágenes desde ViewModel: ${e.message}")
                onComplete(emptyList()) // 🔹 En caso de error, devolvemos una lista vacía
            }
        }
    }
}