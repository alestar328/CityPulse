package com.app.citypulse.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.data.repository.EventRepository
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    // Contiene eventos desde Firestore.
    val eventList: SnapshotStateList<EventEntity> = mutableStateListOf()

    // Carga eventos desde Firestore.
    init {
        loadEvents()
    }

    fun createEvent(event: EventEntity) {
        // Ejectura en segundo plano sin bloquear la UI.
        viewModelScope.launch {
            repository.addEvent(event)
        }
    }

    private fun loadEvents() {
        repository.getEvents { events ->
            eventList.clear()
            eventList.addAll(events)
        }
    }
}
