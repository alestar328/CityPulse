package com.app.citypulse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.citypulse.data.dataUsers.SubcatItem
import com.app.citypulse.data.enums.TipoCategoria
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        viewModelScope.launch {
            try {
                val snaps = firestore.collection("users")
                    .document(uid)
                    .collection("subcategories")
                    .orderBy("createdAt")
                    .get()
                    .await()
                _subcats.value = snaps.documents.map { doc ->
                    val catName = doc.getString("category") ?: ""
                    SubcatItem(
                        id = doc.id,
                        name = doc.getString("name").orEmpty(),
                        category = TipoCategoria.values()
                            .find { it.name == catName }
                            ?: TipoCategoria.NONE,
                        createdAt = doc.getTimestamp("createdAt")
                    )
                }
            } catch (_: Exception) { /* opcional: _errorMessage */
            }

        }
    }
    fun updateSubcategory(id: String, newName: String, newCategory: TipoCategoria) {
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
                            "category" to newCategory.name
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

    fun addSubcategory(name: String, category: TipoCategoria) {
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
                val data = mapOf(
                    "id" to id,
                    "name" to name.trim(),
                    "category" to category.name,
                    "createdAt" to com.google.firebase.Timestamp.now()
                )

                // Guardamos bajo ese ID
                subcatRef.set(data).await()

                // 3) Informamos de éxito (no cerramos el diálogo aún)
                _successMessage.value = "Subcategoría “${name.trim()}” guardada."
                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value = "Error al guardar: ${e.message}"
            }
        }
    }
}