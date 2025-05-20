package com.app.citypulse.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
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
    onAdd: (name: String, category: TipoCategoria, imageUri: Uri?, description: String) -> Unit,
    settingsViewModel: SettingsViewModel = viewModel()

) {
    if (!show) return

    var name by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf(TipoCategoria.GASTRONOMICO) }
    var action by remember { mutableStateOf(SubcatAction.ADD) }
    var selectedSubcat by remember { mutableStateOf<SubcatItem?>(null) }
    var selectionError by remember { mutableStateOf<String?>(null) }

    var localImageUri by remember { mutableStateOf<Uri?>(null) }
    var remoteImageUrl by remember { mutableStateOf<String?>(null) }
// Launcher para abrir galería

    val imagePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> localImageUri = uri }


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
                                    when (act) {
                                        SubcatAction.ADD -> {
                                            // en ADD borramos toda imagen
                                            localImageUri = null
                                            remoteImageUrl = null
                                            description    = ""
                                        }
                                        SubcatAction.UPDATE,
                                        SubcatAction.DELETE -> {
                                            // en UPDATE/DELETE, si hay una subcategoría previamente seleccionada,
                                            // recargamos su image:
                                            selectedSubcat?.image?.let { img ->
                                                remoteImageUrl = img
                                                localImageUri = null
                                                description    = description ?: ""
                                            }
                                        }
                                    }
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
                                    remoteImageUrl = subcat.image
                                    localImageUri = null
                                    description     = subcat.description ?: ""
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
                Text("Descripción corta (máx 20 palabras)", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = description,
                    onValueChange = { new ->
                        // solo permitimos hasta 20 palabras
                        if (new.trim().split("\\s+".toRegex()).size <= 20) {
                            description = new
                        }
                    },
                    placeholder = { Text("Escribe hasta 20 palabras…") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4
                )
                Text(
                    text = "${description.trim().split("\\s+".toRegex()).size}/20 palabras",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )

                Text(text = "Imagen asociada", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                PhotoContainer(
                    url = remoteImageUrl,
                    localUri = localImageUri,
                    onClick = {
                        imagePicker.launch("image/*")
                    },
                    onDelete = {
                        localImageUri = null
                        remoteImageUrl = null
                    },
                    modifier = Modifier
                        .height(170.dp)
                        .fillMaxWidth()
                )



                Spacer(modifier = Modifier.height(16.dp))
                val selErr = selectionError

                when {
                    errorMessage != null -> Text(errorMessage, color = Color.Red)
                    selErr != null -> Text(selErr, color = Color.Red)
                    successMessage != null -> Text(successMessage, color = Color(0xFF4CAF50))
                }
                Spacer(modifier = Modifier.height(16.dp))
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
                                SubcatAction.ADD -> {
                                    onAdd(name, selectedCat, localImageUri, description.trim())
                                }
                                SubcatAction.UPDATE -> {
                                    selectedSubcat?.let { subcat ->
                                        settingsViewModel.updateSubcategory(
                                            id        = subcat.id,
                                            newName   = name.trim(),
                                            newCategory = selectedCat,
                                            newDescription= description.trim()
                                        )
                                    }
                                }
                                SubcatAction.DELETE -> {
                                    // aquí borras la subcategoría completa
                                    settingsViewModel.deleteSubcategory(
                                        name     = name.trim(),
                                        category = selectedCat
                                    )
                                }
                            }
                        },
                        enabled = when (action) {
                            SubcatAction.ADD    -> name.isNotBlank()
                            SubcatAction.UPDATE -> selectedSubcat != null && name.isNotBlank()
                            SubcatAction.DELETE -> selectedSubcat != null
                        },
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