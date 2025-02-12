package com.app.citypulse.presentation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.data.model.EventEntity
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
    var fechaFin by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var aforo by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // Recuperar valores de ubicación al volver de la selección de ubicación
    LaunchedEffect(navController.currentBackStackEntry) {
        savedStateHandle?.get<String>("direccion")?.let {
            lugar = it
            savedStateHandle.remove<String>("direccion")
        }
        savedStateHandle?.get<Double>("latitud")?.let {
            latitud = it
            savedStateHandle.remove<Double>("latitud")
        }
        savedStateHandle?.get<Double>("longitud")?.let {
            longitud = it
            savedStateHandle.remove<Double>("longitud")
        }
    }

    // Fondo con degradado
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
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear evento",
                style = TextStyle(color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(value = nombre, label = "Nombre evento", onValueChange = { nombre = it })
            CustomTextField(value = categoria, label = "Categoría Principal", onValueChange = { categoria = it })
            CustomTextField(value = descripcion, label = "Descripción", onValueChange = { descripcion = it })
            CustomTextField(value = precio, label = "Precio", onValueChange = { precio = it })
            CustomTextField(value = aforo, label = "Aforo", onValueChange = { aforo = it })

            DateTimePickerField(label = "Fecha y hora inicial", dateTime = fechaInicio) { fechaInicio = it }
            DateTimePickerField(label = "Fecha y hora final", dateTime = fechaFin) { fechaFin = it }

            CustomTextField(value = lugar, label = "Ubicación", onValueChange = {}, enabled = false)

            Button(
                onClick = { navController.navigate("location_picker_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Escoger ubicación", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar", color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (nombre.isNotEmpty() && categoria.isNotEmpty() && descripcion.isNotEmpty() &&
                            fechaInicio.isNotEmpty() && fechaFin.isNotEmpty() && precio.isNotEmpty() && aforo.isNotEmpty()) {

                            val event = EventEntity(
                                nombre = nombre,
                                categoria = categoria,
                                descripcion = descripcion,
                                fechaInicio = parseDate(fechaInicio),
                                fechaFin = parseDate(fechaFin),
                                precio = precio.toDoubleOrNull() ?: 0.0,
                                aforo = aforo.toIntOrNull() ?: 0,
                                lugar = lugar,
                                latitud = latitud,
                                longitud = longitud

                            )
                            viewModel.createEvent(event)
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Crear", color = Color.White)
                }

            }
        }
    }
}

// Función para convertir String a Date
fun parseDate(dateStr: String): Date? {
    return try {
        val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(dateStr)
    } catch (e: Exception) {
        null
    }
}


@Composable
fun DateTimePickerField(label: String, dateTime: String, onDateTimeSelected: (String) -> Unit) {
    val context = LocalContext.current

    OutlinedButton(
        onClick = {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            val selectedDate = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth, hourOfDay, minute)
                            }
                            val formattedDate = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                                .format(selectedDate.time)
                            onDateTimeSelected(formattedDate)
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
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Text(
            text = if (dateTime.isEmpty()) label else dateTime,
            color = Color.Blue
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(value: String, label: String, onValueChange: (String) -> Unit, enabled: Boolean = true) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            unfocusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.LightGray,
            disabledContainerColor = Color.Gray.copy(alpha = 0.1f),
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray
        ),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp)
    )
}