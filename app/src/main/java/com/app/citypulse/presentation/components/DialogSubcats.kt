package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.citypulse.data.dataUsers.SubcatItem
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSubcats(
    show: Boolean,
    onDismiss: () -> Unit,
    successMessage: String?,
    errorMessage: String?,
    onAdd: (name: String, category: TipoCategoria) -> Unit,
    settingsViewModel: SettingsViewModel = viewModel()

) {
    if (!show) return

    var name by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCat by remember { mutableStateOf(TipoCategoria.GASTRONOMICO) }
    var action by remember { mutableStateOf(SubcatAction.ADD) }
    var selectedSubcat by remember { mutableStateOf<SubcatItem?>(null) }
    var selectionError by remember { mutableStateOf<String?>(null) }

    //Busqueda predictiva
    val subcats by settingsViewModel.subcats.collectAsState()

    val trimmed = name.trim()

    val suggestions by remember(subcats, trimmed) {
        derivedStateOf {
            if (trimmed.isEmpty()) emptyList()
            else subcats.filter {
                it.name.contains(trimmed, ignoreCase = true)
                        // ¡excluimos la coincidencia exacta para que al elegirla desaparezca la lista!
                        && !it.name.equals(trimmed, ignoreCase = true)
            }
        }
    }

    val expandedSuggestions by remember(suggestions) {
        derivedStateOf { suggestions.isNotEmpty() }
    }


    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                // Título
                Text(
                    text = "Manejo Subcategorías",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))

                // ACCION RADIOS
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SubcatAction.entries.forEach { act ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RadioButton(
                                selected = action == act,
                                onClick = {
                                    action = act
                                    selectedSubcat = null
                                    name = ""
                                }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = act.label)
                        }
                    }
                }

                //RADIOS hasta aqui


                Spacer(modifier = Modifier.height(12.dp))
                // Input Nombre
                Text(text = "Nombre", style = MaterialTheme.typography.bodyMedium)


                ExposedDropdownMenuBox(
                    expanded = expandedSuggestions,
                    onExpandedChange = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (action != SubcatAction.UPDATE) {
                                selectedSubcat = null
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        placeholder = { Text("Escribe para buscar…") }

                    )
                    ExposedDropdownMenu(
                        expanded = expandedSuggestions,
                        onDismissRequest = { }
                    ) {
                        suggestions.forEach { subcat ->
                            DropdownMenuItem(
                                text = { Text(subcat.name) },
                                onClick = {
                                    name = subcat.name
                                    selectedCat = subcat.category
                                    selectedSubcat = subcat
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dropdown Categoría
                Text(text = "Categoría", style = MaterialTheme.typography.bodyMedium)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedCat.displayName ?: selectedCat.name,
                        onValueChange = { },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        TipoCategoria.entries
                            .filter { it != TipoCategoria.NONE }
                            .forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.displayName ?: cat.name) },
                                    onClick = {
                                        selectedCat = cat
                                        expanded = false
                                    }
                                )
                            }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                val selErr = selectionError

                when {
                    errorMessage != null -> Text(errorMessage, color = Color.Red)
                    selErr != null -> Text(selErr, color = Color.Red)
                    successMessage != null -> Text(successMessage, color = Color(0xFF4CAF50))
                }
                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            when (action) {
                                SubcatAction.ADD ->
                                    settingsViewModel.addSubcategory(name, selectedCat)

                                SubcatAction.DELETE ->
                                    settingsViewModel.deleteSubcategory(name, selectedCat)

                                SubcatAction.UPDATE -> {
                                    val item = selectedSubcat
                                    if (item != null)
                                        settingsViewModel.updateSubcategory(
                                            item.id,
                                            name,
                                            selectedCat
                                        )
                                    else
                                        selectionError =
                                            "Selecciona una subcategoría para modificar"
                                }
                            }
                        },
                        enabled = name.isNotBlank()
                                && (action != SubcatAction.UPDATE || selectedSubcat != null),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            when (action) {
                                SubcatAction.ADD -> "Agregar"
                                SubcatAction.DELETE -> "Borrar"
                                SubcatAction.UPDATE -> "Modificar"
                            }
                        )
                    }
                }
            }
        }
    }
}

// Nuevo enum para acción
enum class SubcatAction(val label: String) {
    ADD("Agregar"),
    DELETE("Borrar"),
    UPDATE("Modificar")
}