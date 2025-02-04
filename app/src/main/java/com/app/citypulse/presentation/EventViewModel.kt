package com.app.citypulse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.data.repository.EventRepository
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    fun createEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.addEvent(event)
        }
    }
}