package com.app.citypulse.data.repository

import android.util.Log
import com.app.citypulse.data.model.EventEntity
import com.google.firebase.firestore.FirebaseFirestore

class EventRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addEvent(event: EventEntity) {
        db.collection("Eventos").add(event)
            .addOnSuccessListener { Log.d("Firebase", "Evento agregado con éxito") }
            .addOnFailureListener { Log.e("Firebase", "Error al agregar evento", it) }
    }

    fun getEvents(callback: (List<EventEntity>) -> Unit) {
        db.collection("Eventos")
            .get()
            .addOnSuccessListener { documents ->
                val events = mutableListOf<EventEntity>()
                for (document in documents) {
                    val event = document.toObject(EventEntity::class.java)
                    events.add(event)
                }
                // Devuelve eventos obtenidos.
                callback(events)
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al obtener eventos", exception)
            }
    }
}
