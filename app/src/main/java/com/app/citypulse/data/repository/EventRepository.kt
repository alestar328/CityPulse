package com.app.citypulse.data.repository

import android.net.Uri
import com.app.citypulse.data.model.EventEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventRepository{
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = com.google.firebase.Firebase.storage.reference

    suspend fun addEvent(event: EventEntity): String {
        return try {
            val documentRef = db.collection("Eventos").add(event).await()
            documentRef.update("id", documentRef.id).await()
            documentRef.id // 🔹 Retorna el ID generado para usarlo en la subida de fotos
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
    suspend fun uploadEventImages(eventId: String, uris: List<Uri>, onComplete: (List<String>) -> Unit) {
        val folderRef = storageRef.child("event_images/$eventId")
        val newImageUrls = mutableListOf<String>()

        try {
            // 🔹 Obtener las imágenes actuales en Firestore
            val eventRef = db.collection("Eventos").document(eventId)
            val existingImages = eventRef.get().await().get("galleryPictureUrls") as? MutableList<String> ?: mutableListOf()

            withContext(Dispatchers.IO) {
                uris.map { uri ->
                    async {
                        try {
                            val fileName = "${System.currentTimeMillis()}.jpg"
                            val fileRef = folderRef.child(fileName)

                            println("📤 Subiendo imagen: $fileName")
                            fileRef.putFile(uri).await()
                            val downloadUri = fileRef.downloadUrl.await()
                            println("✅ Imagen subida: $downloadUri")

                            newImageUrls.add(downloadUri.toString())
                        } catch (e: Exception) {
                            println("⚠️ Error al subir imagen: ${e.message}")
                        }
                    }
                }.awaitAll()
            }

            // 🔹 Agregar las nuevas imágenes a las ya existentes
            val allImages = (existingImages + newImageUrls).distinct()

            // 🔹 Guardar en Firestore solo si hay nuevas imágenes
            if (newImageUrls.isNotEmpty()) {
                eventRef.update("galleryPictureUrls", allImages).await()
                println("✅ Imágenes guardadas en Firestore correctamente")
            }

            onComplete(allImages)
        } catch (e: Exception) {
            println("⚠️ Error subiendo imágenes: ${e.message}")
            onComplete(emptyList())
        }
    }



}
