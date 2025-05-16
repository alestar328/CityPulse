package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import com.app.citypulse.data.model.EventUiModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearcherBar(
    modifier: Modifier = Modifier,
    events: List<EventUiModel>, // 🔹 Recibe la lista de eventos desde ViewModel
    onEventSelected: (EventUiModel) -> Unit
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var filteredEvents by remember { mutableStateOf<List<EventUiModel>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager      = LocalFocusManager.current


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
            if (it) keyboardController?.show()
        },
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = searchText,
            onValueChange = { tfv ->
                searchText = tfv
                val query = tfv.text.trim()
                if (query.isNotEmpty()) {
                    val tokens = query.split("\\s+".toRegex()).filter { it.isNotBlank() }
                    filteredEvents = events.filter { event ->
                        tokens.all { token ->
                            listOf(
                                event.nombre,
                                event.nomOrg,
                                event.descripcion,
                                event.subcategoria,
                                event.categoria.displayName.orEmpty()
                            ).any { field ->
                                field.contains(token, ignoreCase = true)
                            }
                        }
                    }
                    expanded = filteredEvents.isNotEmpty()
                } else {
                    filteredEvents = emptyList()
                    expanded = false
                }
            },
            placeholder = { Text("Buscar evento") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor() // <— ancla correctamente el menú
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            filteredEvents.forEach { event ->
                DropdownMenuItem(
                    text = { Text(event.nombre, color = Color.Black) },
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        searchText = TextFieldValue(event.nombre)
                        expanded   = false
                        onEventSelected(event)
                    }
                )
            }
        }
    }
}