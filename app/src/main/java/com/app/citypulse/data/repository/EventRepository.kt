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
    suspend fun uploadEventImages(eventId: String, uris: List<Uri>): List<String> {
        val folderRef = storageRef.child("event_images/$eventId")
        val uploadedImageUrls = mutableListOf<String>()

        try {
            val eventRef = db.collection("Eventos").document(eventId)
            val existingImages =
                eventRef.get().await().get("galleryPictureUrls") as? MutableList<String>
                    ?: mutableListOf()

            val newImages = withContext(Dispatchers.IO) {
                uris.mapIndexed { index, uri ->
                    async {
                        try {
                            val fileName = "image_${index}_${System.currentTimeMillis()}.jpg"
                            val fileRef = folderRef.child(fileName)

                            println("📤 Subiendo imagen: $fileName")
                            fileRef.putFile(uri).await()
                            val downloadUri = fileRef.downloadUrl.await()
                            println("✅ Imagen subida: $downloadUri")

                            synchronized(uploadedImageUrls) { uploadedImageUrls.add(downloadUri.toString()) }
                        } catch (e: Exception) {
                            println("⚠️ Error al subir imagen: ${e.message}")
                        }
                    }
                }.awaitAll()
            }

            val finalImageList = (existingImages + uploadedImageUrls).distinct()

            // Guardamos en Firestore solo si se subieron nuevas imágenes
            if (uploadedImageUrls.isNotEmpty()) {
                eventRef.update("galleryPictureUrls", finalImageList).await()
                println("✅ Imágenes guardadas correctamente en Firestore")
            }

            return finalImageList
        } catch (e: Exception) {
            println("⚠️ Error subiendo imágenes: ${e.message}")
            return emptyList()
        }
    }


}
