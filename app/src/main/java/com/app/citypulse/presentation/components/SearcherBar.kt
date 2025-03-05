package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.presentation.ui.theme.TurkBlue


@Composable
fun SearcherBar(
    modifier: Modifier = Modifier,
    events: List<EventUiModel>, // 🔹 Recibe la lista de eventos desde ViewModel
    onEventSelected: (EventUiModel) -> Unit
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var filteredEvents by remember { mutableStateOf<List<EventUiModel>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) } // 🔹

    LaunchedEffect(searchText) {
        // 🔹 Filtra eventos mientras el usuario escribe
        filteredEvents = if (searchText.text.isNotEmpty()) {
            events.filter { event ->
                event.nombre.contains(searchText.text, ignoreCase = true) ||
                        event.lugar.contains(searchText.text, ignoreCase = true)
            }
        } else emptyList()
        expanded = filteredEvents.isNotEmpty() // 🔹 Muestra sugerencias solo si hay resultados
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = TurkBlue, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp)
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(12.dp)) // Espaciado entre el icono y el campo de texto

            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                placeholder = { Text("Buscar evento", color = Color.White, fontSize = 18.sp) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }

        // 🔹 Dropdown para mostrar sugerencias en tiempo real
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            filteredEvents.forEach { event ->
                DropdownMenuItem(
                    text = { Text(event.nombre, color = Color.Black) },
                    onClick = {
                        searchText = TextFieldValue(event.nombre) // 🔹 Muestra el evento seleccionado
                        expanded = false
                        onEventSelected(event) // 🔹 Llama al callback para manejar la selección
                    }
                )
            }
        }
    }
}
