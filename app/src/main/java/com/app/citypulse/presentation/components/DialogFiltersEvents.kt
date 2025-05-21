package com.app.citypulse.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.data.model.EventFilters
import com.app.citypulse.presentation.viewmodel.SettingsViewModel

enum class FilterOption(val label: String) {
    CATEGORY("Categoria"),
    SUBCATEGORY("Sub categoria"),
    RATING("Valoracion")
}

@Composable
fun DialogFiltersEvents(
    show: Boolean,
    onDismiss: () -> Unit,
    onApply: (EventFilters) -> Unit,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    var selectedCats by remember { mutableStateOf(setOf<TipoCategoria>()) }
    if (!show) return
    var selected by remember { mutableStateOf(FilterOption.CATEGORY) }
    val storedSubcats by settingsViewModel.subcats.collectAsState()
    val allSubcats by settingsViewModel.subcats.collectAsState()
    var selectedStars by remember { mutableStateOf<Float?>(null) }

    val displayedSubcats = remember(allSubcats, selectedCats) {
        if (selectedCats.isEmpty()) {
            allSubcats
        } else {
            allSubcats.filter { it.category in selectedCats }
        }
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
                // Top bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CityPulse",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(
                        TipoCategoria.GASTRONOMICO,
                        TipoCategoria.CULTURAL,
                        TipoCategoria.FIESTA
                    ).forEach { cat ->
                        val checked = selectedCats.contains(cat)
                        FilterChip(
                            selected = checked,
                            onClick = {
                                selectedCats = if (checked)
                                    selectedCats - cat
                                else
                                    selectedCats + cat
                            },
                            label = { Text(cat.displayName ?: cat.name) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                FilterStarsBar(
                    modifier = Modifier.fillMaxWidth(),
                    options = listOf(3.5f, 4.0f, 4.5f),
                    onSelectionChange = { rating ->
                        selectedStars = rating
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                FilterSubcat(
                    title = "Subcategorías",
                    subcats = displayedSubcats,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color(0xFFBDBDBD), thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón "Borrar"
                    OutlinedButton(
                        onClick = {
                            selectedCats = emptySet()
                            selectedStars = null
                        },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color(0xFF1976D2)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF1976D2)
                        )
                    ) {
                        Text("Borrar")
                    }

                    // Botón "Aplicar"
                    Button(
                        onClick = {
                            // 1) Creamos el objeto EventFilters
                            val filters = EventFilters(
                                categories     = selectedCats,
                                subcategories  = displayedSubcats.map { it.name }.toSet(),
                                rating         = selectedStars
                            )
                            // 2) Llamamos al callback
                            onApply(filters)
                            // 3) Cerramos el diálogo
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Aplicar")
                    }
                }
            }

        }
    }
}
