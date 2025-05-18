package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.presentation.ui.theme.TurkBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearcherBar(
    modifier: Modifier = Modifier,
    events: List<EventUiModel>, // 🔹 Recibe la lista de eventos desde ViewModel
    onEventSelected: (EventUiModel) -> Unit,
    onSettingsClick: () -> Unit,
    onClearClick: () -> Unit
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
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        TextField(
            value = searchText,
            onValueChange = { tfv ->

                val clean = tfv.text.replace("\n", "")
                searchText = tfv.copy(text = clean)

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
            placeholder = { Text("Buscar evento", color = Color.White.copy(alpha = 0.6f)) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            leadingIcon = {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                Row {
                    if (searchText.text.isNotEmpty()) {
                        IconButton(onClick = {
                            searchText = TextFieldValue("")
                            onClearClick()
                            expanded = false
                            keyboardController?.show()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar",
                                tint = Color.White
                            )
                        }
                    }
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor     = TurkBlue,
                unfocusedContainerColor   = TurkBlue,
                disabledContainerColor    = TurkBlue,
                focusedTextColor          = Color.White,
                unfocusedTextColor        = Color.White,
                cursorColor               = Color.White,
                focusedIndicatorColor     = Color.Transparent,
                unfocusedIndicatorColor   = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
                expanded = false
            })
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(
                    color = TurkBlue,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            filteredEvents.forEach { event ->
                DropdownMenuItem(
                    text = { Text(event.nombre, color = Color.White) },
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        searchText = TextFieldValue(event.nombre)
                        expanded   = false
                        onEventSelected(event)
                    },
                    colors = MenuItemColors(
                        textColor                = Color.White,
                        leadingIconColor         = Color.White,
                        trailingIconColor        = Color.White,
                        disabledTextColor        = Color.White,
                        disabledLeadingIconColor = Color.White,
                        disabledTrailingIconColor= Color.White
                    )
                )
            }
        }
    }
}