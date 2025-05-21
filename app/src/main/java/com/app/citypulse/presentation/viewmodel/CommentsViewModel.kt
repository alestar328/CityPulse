package com.app.citypulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.model.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date


class CommentsViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    fun fetchComments(eventId: String) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("Eventos")
                    .document(eventId)
                    .collection("comments")
                    .orderBy("createdAt")
                    .get()
                    .await()

                val loaded = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Comment::class.java)
                        ?.copy(id = doc.id)
                }
                _comments.value = loaded
            } catch (e: Exception) {
                _comments.value = emptyList()
            }
        }
    }
    fun updateComment(
        eventId: String,
        commentId: String,
        newRating: Int,
        newMessage: String?,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val data = mapOf(
                    "rating" to newRating,
                    "message" to newMessage,
                    // Opcional: actualizar timestamp?
                    "updatedAt" to Date()
                )
                firestore.collection("Eventos")
                    .document(eventId)
                    .collection("comments")
                    .document(commentId)
                    .update(data)
                    .await()
                fetchComments(eventId)
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
    fun deleteComment(
        eventId: String,
        commentId: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                firestore.collection("Eventos")
                    .document(eventId)
                    .collection("comments")
                    .document(commentId)
                    .delete()
                    .await()
                fetchComments(eventId)
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
    fun addComment(eventId: String, rating: Int, organizerId: String ,message: String?, onComplete: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onComplete(false)
        val user = auth.currentUser ?: return onComplete(false)

        val comment = Comment(
            id = "", // Firestore will generate the ID
            eventId = eventId,
            userId = uid,
            organizerId = organizerId,
            userName = user.displayName ?: "Usuario",
            userProfileUrl = user.photoUrl?.toString(),
            message = message,
            rating = rating,
            createdAt = Date()
        )

        viewModelScope.launch {
            try {
                firestore.collection("Eventos")
                    .document(eventId)
                    .collection("comments")
                    .add(comment)
                    .await()
                fetchComments(eventId)
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
}
