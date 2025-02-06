package com.app.citypulse.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.data.repository.EventRepository
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    val eventList: SnapshotStateList<EventEntity> = mutableStateListOf()

    init {
        loadEvents()
    }

    fun createEvent(event: EventEntity) {
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
