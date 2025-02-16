package com.app.citypulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.data.repository.EventRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventList = MutableStateFlow<List<EventEntity>>(emptyList())
    val eventList: StateFlow<List<EventEntity>> = _eventList

    init {
        loadEvents()
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

    fun loadEvents() {
        repository.getEvents { events ->
            _eventList.value = emptyList()
            _eventList.value = events
        }
    }
}
