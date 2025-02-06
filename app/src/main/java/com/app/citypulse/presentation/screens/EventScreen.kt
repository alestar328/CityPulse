package com.app.citypulse.presentation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.presentation.EventViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateEventScreen(viewModel: EventViewModel, navController: NavController) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var lugar by rememberSaveable { mutableStateOf("Ubicación no seleccionada") }
    var latitud by rememberSaveable { mutableStateOf(0.0) }
    var longitud by rememberSaveable { mutableStateOf(0.0) }
    var fechaInicio by rememberSaveable { mutableStateOf("") }
    var horaInicio by rememberSaveable { mutableStateOf("") }
    var fechaFin by rememberSaveable { mutableStateOf("") }
    var horaFin by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var aforo by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del Evento") })
        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
        TextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        TextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") })
        TextField(value = aforo, onValueChange = { aforo = it }, label = { Text("Aforo") })

        Button(onClick = { navController.navigate("location_picker_screen") }) {
            Text("Escoger Ubicación")
        }

        Text(text = "Ubicación: $lugar")

        Button(onClick = { showDateTimePicker(context, { fechaInicio = it }, { horaInicio = it }) }) {
            Text("Seleccionar Fecha y Hora de Inicio")
        }
        Text("Inicio: $fechaInicio $horaInicio")

        Button(onClick = { showDateTimePicker(context, { fechaFin = it }, { horaFin = it }) }) {
            Text("Seleccionar Fecha y Hora de Fin")
        }
        Text("Fin: $fechaFin $horaFin")

        Button(onClick = {
            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val fechaInicioDate = format.parse("$fechaInicio $horaInicio")
            val fechaFinDate = format.parse("$fechaFin $horaFin")

            val event = EventEntity(
                nombre = nombre,
                categoria = categoria,
                descripcion = descripcion,
                lugar = lugar,
                latitud = latitud,
                longitud = longitud,
                fechaInicio = fechaInicioDate,
                fechaFin = fechaFinDate,
                precio = precio.toDoubleOrNull() ?: 0.0,
                aforo = aforo.toIntOrNull() ?: 0
            )
            viewModel.createEvent(event)
            navController.popBackStack()
        }) {
            Text("Crear Evento")
        }
    }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.getLiveData<Double>("latitud")?.observeForever { latitud = it }
    savedStateHandle?.getLiveData<Double>("longitud")?.observeForever { longitud = it }
    savedStateHandle?.getLiveData<String>("direccion")?.observeForever { lugar = it }
}

fun showDateTimePicker(
    context: Context,
    updateDate: (String) -> Unit,
    updateTime: (String) -> Unit
) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context, { _, year, month, dayOfMonth ->
            val fecha = "$dayOfMonth/${month + 1}/$year"
            updateDate(fecha)

            TimePickerDialog(
                context, { _, hour, minute ->
                    val hora = String.format("%02d:%02d", hour, minute)
                    updateTime(hora)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}
