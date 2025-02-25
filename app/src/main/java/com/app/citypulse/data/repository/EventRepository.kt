package com.app.citypulse.data.repository

import com.app.citypulse.data.model.EventEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRepository {

private val db = FirebaseFirestore.getInstance()

suspend fun addEvent(event: EventEntity) {
    try {
        val eventData = hashMapOf(
            "id" to event.id,
            "nombre" to event.nombre,
            "categoria" to event.categoria.name,
            "subcategoria" to event.subcategoria,
            "descripcion" to event.descripcion,
            "lugar" to event.lugar,
            "latitud" to event.latitud,
            "longitud" to event.longitud,
            "fechaInicio" to event.fechaInicio,
            "fechaFin" to event.fechaFin,
            "aforo" to event.aforo,
            "precio" to event.precio,
            "valoracion" to event.valoracion,
            "idRealizador" to event.idRealizador
        )
        val documentRef = db.collection("Eventos").add(eventData).await()
        documentRef.update("id", documentRef.id)
    } catch (e: Exception) {
        throw e
    }
}

suspend fun deleteEvent(eventId: String) {
    try {
        db.collection("Eventos").document(eventId).delete().await()
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
                val event = doc.toObject(EventEntity::class.java)?.copy(
                    id = doc.id,
                    idRealizador = doc.get("idRealizador")?.toString() ?: ""
                )
                event
            } ?: emptyList()

            callback(events)
        }
    }
}
