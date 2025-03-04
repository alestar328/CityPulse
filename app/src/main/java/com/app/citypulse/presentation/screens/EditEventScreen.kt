package com.app.citypulse.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Brush
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.presentation.components.CustomTextField
import com.app.citypulse.presentation.components.DescriptionTextField
import com.app.citypulse.presentation.components.NumericTextField
import com.app.citypulse.presentation.viewmodel.EventViewModel
import java.text.SimpleDateFormat

@Composable
fun EditEventScreen(eventId: String, viewModel: EventViewModel, navController: NavController) {
    val context = LocalContext.current

    val eventState by viewModel.eventFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getEventById(eventId)
    }

    if (eventState == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var nombre by rememberSaveable { mutableStateOf("") }
    var categoriaSeleccionada by rememberSaveable { mutableStateOf(TipoCategoria.CULTURAL) }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var lugar by rememberSaveable { mutableStateOf("Ubicación no seleccionada") }
    var latitud by rememberSaveable { mutableStateOf(0.0) }
    var longitud by rememberSaveable { mutableStateOf(0.0) }
    var fechaInicio by rememberSaveable { mutableStateOf("Sin fecha") }
    var fechaFin by rememberSaveable { mutableStateOf("Sin fecha") }
    var precio by rememberSaveable { mutableStateOf("") }
    var aforo by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(eventState) {
        eventState?.let {
            nombre = it.nombre
            categoriaSeleccionada = it.categoria
            descripcion = it.descripcion
            lugar = it.lugar
            latitud = it.latitud
            longitud = it.longitud
            fechaInicio = formatDate(it.fechaInicio)
            fechaFin = formatDate(it.fechaFin)
            precio = it.precio.toString()
            aforo = it.aforo.toString()
        }
    }

    var fechaInicioCalendar by rememberSaveable { mutableStateOf<Calendar?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.DarkGray, Color.Black)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Editar evento",
                style = TextStyle(color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(value = nombre, label = "Nombre del evento", onValueChange = { nombre = it })
            CategoriaDropdown(selectedCategoria = categoriaSeleccionada) { categoriaSeleccionada = it }
            DescriptionTextField(value = descripcion, label = "Descripción", onValueChange = { descripcion = it })
            NumericTextField(value = precio, label = "Precio", onValueChange = { precio = it }, isDecimal = true)
            NumericTextField(value = aforo, label = "Aforo", onValueChange = { aforo = it })

            DateTimePickerField(
                label = "Fecha y hora inicial",
                dateTime = fechaInicio,
                onDateTimeSelected = { selectedDate ->
                    fechaInicio = selectedDate
                    fechaInicioCalendar = Calendar.getInstance().apply { time = parseDate(selectedDate) }
                }
            )

            if (fechaInicioCalendar != null) {
                DateTimePickerField(
                    label = "Fecha y hora final",
                    dateTime = fechaFin,
                    minDate = fechaInicioCalendar,
                    onDateTimeSelected = { fechaFin = it }
                )
            }

            CustomTextField(value = lugar, label = "Ubicación", onValueChange = {}, enabled = false)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Cancelar")
                }

                Button(onClick = {
                    if (nombre.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty() || precio.isEmpty() || aforo.isEmpty()) {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (descripcion.length !in 200..240) {
                        Toast.makeText(context, "La descripción debe tener entre 200 y 240 caracteres", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val updatedEvent = eventState?.copy(
                        nombre = nombre,
                        categoria = categoriaSeleccionada,
                        descripcion = descripcion,
                        lugar = lugar,
                        latitud = latitud,
                        longitud = longitud,
                        fechaInicio = parseDate(fechaInicio),
                        fechaFin = parseDate(fechaFin),
                        precio = precio.toDouble(),
                        aforo = aforo.toInt()
                    )

                    updatedEvent?.let { viewModel.updateEvent(it) }
                    navController.popBackStack()
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green)) {
                    Text("Guardar cambios")
                }
            }
        }
    }
}

fun formatDate(date: Date?): String {
    return if (date != null) {
        val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        formatter.format(date)
    } else {
        "Sin fecha"
    }
}