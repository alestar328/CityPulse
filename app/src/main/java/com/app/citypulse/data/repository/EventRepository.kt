package com.app.citypulse.data.repository

import android.net.Uri
import com.app.citypulse.data.model.EventEntity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventRepository{
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference
    private fun storagePath(subpath: String) = storageRef.child(subpath)

    suspend fun addEvent(event: EventEntity): String {
        return try {
            val newEventRef = db.collection("Eventos").document() // Genera una referencia de documento válida
            val eventWithId = event.copy(id = newEventRef.id) // Copia el evento con el nuevo ID

            newEventRef.set(eventWithId).await() // Guarda el evento en Firestore
            newEventRef.id
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

    suspend fun updateEvent(event: EventEntity) {
        try {
            db.collection("Eventos").document(event.id).set(event).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getEventById(eventId: String): EventEntity? {
        return try {
            val document = db.collection("Eventos").document(eventId).get().await()
            document.toObject(EventEntity::class.java)?.copy(
                id = document.id,
                fechaInicio = document.getTimestamp("fechaInicio")?.toDate(),
                fechaFin = document.getTimestamp("fechaFin")?.toDate()
            )
        } catch (e: Exception) {
            null
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
                    event?.copy(id = doc.id)
                } ?: emptyList()

                callback(events)
            }
    }
    suspend fun uploadEventImages(eventId: String, uris: List<Uri>): List<String> {
        // 1) Carpeta en Storage: /events/{eventId}/...
        val folderRef = storagePath("events/$eventId")
        val uploadedImageUrls = mutableListOf<String>()

        try {
            val eventDocRef = db.collection("Eventos").document(eventId)

            // 2) Leemos URLs ya guardadas (campo galleryPictureUrls)
            val existingImages = eventDocRef
                .get()
                .await()
                .get("galleryPictureUrls") as? List<String>
                ?: emptyList()

            // 3) Subida concurrente de todas las URIs
            withContext(Dispatchers.IO) {
                uris.mapIndexed { index, uri ->
                    async {
                        try {
                            val timestamp = System.currentTimeMillis()
                            val fileName  = "photo_$timestamp.jpg"
                            val fileRef   = folderRef.child(fileName)

                            // 3.1) Subir a Storage
                            fileRef.putFile(uri).await()
                            val downloadUrl = fileRef.downloadUrl.await().toString()

                            // 3.2) Guardamos en memoria para luego actualizar el array
                            synchronized(uploadedImageUrls) {
                                uploadedImageUrls.add(downloadUrl)
                            }

                            // 3.3) Creamos doc en la sub-colección "eventGallery"
                            val photoData = mapOf(
                                "url"       to downloadUrl,
                                "createdAt" to com.google.firebase.Timestamp.now(),
                                "order"     to index
                            )
                            eventDocRef
                                .collection("eventGallery")
                                .document(fileName)       // ID igual al nombre de fichero
                                .set(photoData)
                                .await()
                        } catch (e: Exception) {
                            println("⚠️ Error subiendo imagen #$index: ${e.message}")
                        }
                    }
                }.awaitAll()
            }

            // 4) Actualizamos el campo galleryPictureUrls en el doc principal
            val finalList = (existingImages + uploadedImageUrls).distinct()
            if (uploadedImageUrls.isNotEmpty()) {
                eventDocRef
                    .update("galleryPictureUrls", finalList)
                    .await()
            }

            return finalList

        } catch (e: Exception) {
            println("⚠️ Error en uploadEventImages: ${e.message}")
            return emptyList()
        }
    }
    suspend fun getEventGallery(eventId: String): List<String> {
        return try {
            val snaps = db
                .collection("Eventos")
                .document(eventId)
                .collection("eventGallery")
                .orderBy("order")
                .get()
                .await()
            snaps.documents.mapNotNull { it.getString("url") }
        } catch (e: Exception) {
            println("⚠️ Error en getEventGallery: ${e.message}")
            emptyList()
        }
    }

}
