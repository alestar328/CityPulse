package com.app.citypulse.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.citypulse.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSubcat(
    subcategoria: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    onValueChange: (String) -> Unit,
    placeholderColor: Color = Color.LightGray,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    val subcats by settingsViewModel.subcats.collectAsState()
    val trimmed = subcategoria.trim()

    // Filtrado en deriveState para rendimiento
    val suggestions by remember(subcats, trimmed) {
        derivedStateOf {
            if (trimmed.isEmpty()) emptyList()
            else subcats.filter {
                it.name.contains(trimmed, ignoreCase = true)
                        // excluimos la coincidencia exacta
                        && !it.name.equals(trimmed, ignoreCase = true)
            }
        }
    }
    // Mostrar menú sólo si hay sugerencias

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = subcategoria,
            onValueChange = {
                onValueChange(it)
                // Si hay sugerencias, abrimos el menú
                if (it.trim().isNotEmpty() && suggestions.isNotEmpty()) {
                    expanded = true
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            placeholder = {
                Text(
                    text="Escribe para buscar subcategoría…",
                    color = placeholderColor) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            colors = outlinedTextFieldColors(
                focusedTextColor = textColor,
                cursorColor = textColor,
                focusedBorderColor = textColor.copy(alpha = 0.5f),
                unfocusedBorderColor = textColor.copy(alpha = 0.3f)
            )
        )

        // Solo mostramos el menu si está expandido y hay sugerencias
        ExposedDropdownMenu(
            expanded = expanded && suggestions.isNotEmpty(),
            onDismissRequest = { expanded = false },
        ) {
            suggestions.forEach { subcat ->
                DropdownMenuItem(
                    text = { Text(subcat.name) },
                    onClick = {
                        onValueChange(subcat.name)
                        expanded = false
                    }
                )
            }
        }
    }
}