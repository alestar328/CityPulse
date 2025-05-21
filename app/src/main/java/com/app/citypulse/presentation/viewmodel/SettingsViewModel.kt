package com.app.citypulse.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.dataUsers.SubcatItem
import com.app.citypulse.data.enums.TipoCategoria
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SettingsViewModel : ViewModel() {
    private val _showSubcatDialog = MutableStateFlow(false)
    val showSubcatDialog: StateFlow<Boolean> = _showSubcatDialog

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _subcats = MutableStateFlow<List<SubcatItem>>(emptyList())
    val subcats: StateFlow<List<SubcatItem>> = _subcats

    //Carga de elementos
    init {
        loadSubcategories()
    }


    fun loadSubcategories() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .collection("subcategories")
            .orderBy("createdAt")
            .addSnapshotListener { snapshots, e ->
                if (e != null || snapshots == null) {
                    // Manejo básico de error
                    _errorMessage.value = "Error al cargar subcategorías."
                    return@addSnapshotListener
                }

                val subcats = snapshots.documents.map { doc ->
                    SubcatItem(
                        id = doc.id,
                        creatorId = doc.getString("creatorId") ?: uid,
                        name = doc.getString("name").orEmpty(),
                        category = TipoCategoria.values()
                            .find { it.name == doc.getString("category") } ?: TipoCategoria.NONE,
                        createdAt = doc.getTimestamp("createdAt"),
                        image = doc.getString("image"),
                        description = doc.getString("description")
                    )
                }

                _subcats.value = subcats
            }
    }
    fun updateSubcategory(id: String, newName: String, newCategory: TipoCategoria, newDescription: String) {
        val trimmed = newName.trim()
        when {
            trimmed.length > 30 -> {
                _errorMessage.value = "Máximo 30 caracteres."
                return
            }
            trimmed.split("\\s+".toRegex()).size > 2 -> {
                _errorMessage.value = "Máximo 2 palabras."
                return
            }
            trimmed.contains("\\d".toRegex()) -> {
                _errorMessage.value = "No puede contener números."
                return
            }
        }

        val uid = auth.currentUser?.uid ?: run {
            _errorMessage.value = "Usuario no autenticado."
            return
        }

        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(uid)
                    .collection("subcategories")
                    .document(id)
                    .update(
                        mapOf(
                            "name"     to trimmed,
                            "category" to newCategory.name,
                            "description" to newDescription
                        )
                    )
                    .await()

                loadSubcategories()  // refrescar lista
                _successMessage.value = "Subcategoría actualizada."
                _errorMessage.value   = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar: ${e.message}"
            }
        }
    }
    fun deleteSubcategory(name: String, category: TipoCategoria) {
        val trimmed = name.trim()
        // Validación mínima
        if (trimmed.isEmpty()) {
            _errorMessage.value = "Introduce el nombre de la subcategoría a borrar."
            return
        }
        // Buscamos el objeto existente con mismo nombre y categoría
        val toDelete = _subcats.value.find {
            it.name.equals(trimmed, ignoreCase = true) && it.category == category
        }
        if (toDelete == null) {
            _errorMessage.value = "No se encontró esa subcategoría."
            return
        }
        // Borrado
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _errorMessage.value = "Usuario no autenticado."
            return
        }
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(uid)
                    .collection("subcategories")
                    .document(toDelete.id)
                    .delete()
                    .await()
                // actualizamos lista
                loadSubcategories()
                _successMessage.value = "Subcategoría “${trimmed}” borrada."
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al borrar: ${e.message}"
            }
        }
    }

    fun openSubcatDialog() {
        _errorMessage.value = null
        _successMessage.value = null
        _showSubcatDialog.value = true
        loadSubcategories()
    }

    fun closeSubcatDialog() {
        _showSubcatDialog.value = false
    }

    fun addSubcategory(
        name: String,
        category: TipoCategoria,
        imageUri: Uri?,
        description: String
    ) {
        val trimmed = name.trim()

        when {
            trimmed.length > 30 -> {
                _errorMessage.value = "Máximo 30 caracteres."
                return
            }

            trimmed.trim().split("\\s+".toRegex()).size > 2 -> {
                _errorMessage.value = "Máximo 2 palabras."
                return
            }

            trimmed.contains("\\d".toRegex()) -> {
                _errorMessage.value = "No puede contener números."
                return
            }
        }

        if (_subcats.value.any {
                it.name.equals(
                    trimmed,
                    ignoreCase = true
                ) && it.category == category
            }) {
            _errorMessage.value = "Ya existe subcategoría."
            return
        }

        // 2) Persistencia con ID propio
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _errorMessage.value = "Usuario no autenticado."
            return
        }

        viewModelScope.launch {
            try {
                // Creamos un documento con ID generado automáticamente
                val subcatRef = firestore
                    .collection("users")
                    .document(uid)
                    .collection("subcategories")
                    .document() // sin parámetros, Firestore asigna un ID único

                val id = subcatRef.id
                val now = com.google.firebase.Timestamp.now()
                val data = mapOf(
                    "id" to id,
                    "name" to trimmed,
                    "category" to category.name,
                    "description" to description.trim(),
                    "createdAt" to now
                )

                // Guardamos bajo ese ID
                subcatRef.set(data).await()

                // 2) Si el usuario seleccionó una foto, la subimos
                imageUri?.let { uri ->
                    // ruta: users/{uid}/subcategories/{id}/{timestamp}.jpg
                    val filename = "photo_${System.currentTimeMillis()}.jpg"
                    val storageRef = Firebase.storage
                        .reference
                        .child("users/$uid/subcategories/$id/$filename")

                    // subimos y obtenemos URL
                    val uploadTask = storageRef.putFile(uri).await()
                    val downloadUrl = storageRef.downloadUrl.await().toString()

                    // guardamos el campo "image" en Firestore
                    subcatRef.update("image", downloadUrl).await()
                }

                // 3) Refrescamos la lista y mostramos éxito
                loadSubcategories()
                _successMessage.value = "Subcategoría “$trimmed” guardada."
                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value = "Error al guardar: ${e.message}"
            }
        }
    }
}