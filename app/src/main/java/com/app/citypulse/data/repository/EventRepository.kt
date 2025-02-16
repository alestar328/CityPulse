package com.app.citypulse.data.repository

import com.app.citypulse.data.model.EventEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun addEvent(event: EventEntity) {
        try {
            val documentRef = db.collection("Eventos").add(event).await()
            documentRef.update("id", documentRef.id)
        } catch (e: Exception) {
            throw e
        }
    }

    fun getEvents(callback: (List<EventEntity>) -> Unit) {
        db.collection("Eventos")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val events = snapshots?.documents?.mapNotNull { doc ->
                    val event = doc.toObject(EventEntity::class.java)
                    event?.copy(id = doc.id) // 🔹 Asigna manualmente el ID de Firestore
                } ?: emptyList()

                callback(events)
            }
    }
}
