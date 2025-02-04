package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.presentation.EventViewModel

@Composable
fun CreateEventScreen(viewModel: EventViewModel, navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = lugar, onValueChange = { lugar = it }, label = { Text("Lugar") })
        OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha") })

        Button(
            onClick = {
                viewModel.createEvent(
                    EventEntity(nombre = nombre, descripcion = descripcion, lugar = lugar, fecha = fecha)
                )
                navController.popBackStack()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Crear Evento")
        }
    }
}
