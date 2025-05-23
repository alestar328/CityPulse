package com.app.citypulse.presentation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.citypulse.R
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.presentation.components.CustomTextField
import com.app.citypulse.presentation.components.DescriptionTextField
import com.app.citypulse.presentation.components.DropdownSubcat
import com.app.citypulse.presentation.components.NumericTextField
import com.app.citypulse.presentation.components.PhotoContainer
import com.app.citypulse.presentation.ui.theme.TurkBlue
import com.app.citypulse.presentation.ui.theme.YellowLight
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.app.citypulse.presentation.viewmodel.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateEventScreen(
    viewModel: EventViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    innerPadding: PaddingValues,
    settingsViewModel: SettingsViewModel// 1) Inyectamos aquí

) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var categoriaSeleccionada by rememberSaveable { mutableStateOf(TipoCategoria.CULTURAL) }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var lugar by rememberSaveable { mutableStateOf("Ubicación no seleccionada") }
    var latitud by rememberSaveable { mutableStateOf(0.0) }
    var longitud by rememberSaveable { mutableStateOf(0.0) }
    var fechaInicio by rememberSaveable { mutableStateOf("") }
    var fechaFin by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var aforo by rememberSaveable { mutableStateOf("") }
    var eventPhotos by rememberSaveable { mutableStateOf<List<Uri>>(emptyList()) }
    val selectedPhotos = remember { mutableStateListOf<Uri?>(null, null, null) }
    var subcategoria by rememberSaveable { mutableStateOf("") }
    val currentUser by authViewModel.currentUser.collectAsState()

    val gallery by viewModel.galleryUrls.collectAsState()

    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    // Cargar los textos antes del onClick
    val errorDescripcion = stringResource(id = R.string.error_descripcion)
    val errorUbicacion = stringResource(id = R.string.error_ubicacion)
    val errorCampos = stringResource(id = R.string.error_campos)

    var nomOrg by rememberSaveable {
        mutableStateOf(
            currentUser?.let { "${it.name} ${it.surname}" }.orEmpty()
        )
    }
    LaunchedEffect(navController.currentBackStackEntry) {
        val savedState = navController.currentBackStackEntry?.savedStateHandle

        savedState?.get<Double>("latitud")?.let {
            if (it != 0.0) latitud = it
        }
        savedState?.get<Double>("longitud")?.let {
            if (it != 0.0) longitud = it
        }
        savedState?.get<String>("direccion")?.let {
            if (it.isNotEmpty()) lugar = it
        }

        savedState?.get<List<Uri>>("event_photos")?.let {
            if (it.isNotEmpty()) eventPhotos = it
        }
    }
    LaunchedEffect(eventPhotos) {
        eventPhotos.take(3).forEachIndexed { i, uri ->
            selectedPhotos[i] = uri
        }
    }
    val photoLaunchers = List(3) { index ->
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedPhotos[index] = uri
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.DarkGray, Color.Black)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.crear_evento),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                CustomTextField(
                    value = nombre,
                    label = stringResource(id = R.string.nombre_evento),
                    onValueChange = { nombre = it })

                CategoriaDropdown(selectedCategoria = categoriaSeleccionada) {
                    categoriaSeleccionada = it
                }
                DropdownSubcat(
                    subcategoria = subcategoria,
                    onValueChange = { subcategoria = it },
                    settingsViewModel = settingsViewModel
                )

                DescriptionTextField(
                    value = descripcion,
                    label = stringResource(id = R.string.descripcion),
                    onValueChange = { descripcion = it })

                NumericTextField(
                    value = precio,
                    label = stringResource(id = R.string.precio),
                    onValueChange = { precio = it },
                    isDecimal = true
                )

                NumericTextField(
                    value = aforo,
                    label = stringResource(id = R.string.aforo),
                    onValueChange = { aforo = it })
            }
            item {
                var fechaInicioCalendar by rememberSaveable { mutableStateOf<Calendar?>(null) }

                DateTimePickerField(
                    label = stringResource(id = R.string.fecha_inicio),
                    dateTime = fechaInicio,
                    onDateTimeSelected = { selectedDate ->
                        fechaInicio = selectedDate
                        fechaInicioCalendar = Calendar.getInstance().apply {
                            time = parseDate(selectedDate)
                        }
                    }
                )

                // Solo mostrar el selector de fecha final cuando la fecha de inicio ya ha sido seleccionada
                if (fechaInicioCalendar != null) {
                    DateTimePickerField(
                        label = stringResource(id = R.string.fecha_fin),
                        dateTime = fechaFin,
                        minDate = fechaInicioCalendar ?: Calendar.getInstance(),
                        onDateTimeSelected = { selectedDate ->
                            fechaFin = selectedDate
                        }
                    )
                }

                CustomTextField(
                    value = lugar,
                    label = stringResource(id = R.string.ubicacion),
                    onValueChange = {},
                    enabled = false
                )

                Button(
                    onClick = { navController.navigate("location_picker_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = YellowLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.escoger_ubicacion), color = TurkBlue)
                }
            }
            item {
                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate("map_screen") {
                                popUpTo("map_screen") { inclusive = false }
                            }
                        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.cancelar), color = TurkBlue)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        selectedPhotos.forEachIndexed { index, uri ->
                            PhotoContainer(
                                url = null, // No cargamos desde URL porque la imagen aún no está en Firebase
                                localUri = uri,
                                onClick = { photoLaunchers[index].launch("image/*") }, // Abrir galería al hacer clic
                                onDelete = {
                                    selectedPhotos[index] = null
                                } // Borrar imagen seleccionada
                            )
                        }

                    }
                    Spacer(modifier = Modifier.width(5.dp))

                }
            }
            item {
                if (gallery.isNotEmpty()) {
                    Text("Fotos guardadas:", color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        items(gallery) { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = "Foto del evento",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(MaterialTheme.shapes.small),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                navController.navigate("map_screen") {
                                    popUpTo("map_screen") { inclusive = false }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(id = R.string.cancelar), color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(5.dp))

                        Button(
                            onClick = {
                                if (nombre.isNotEmpty() && fechaInicio.isNotEmpty() && fechaFin.isNotEmpty() &&
                                    precio.isNotEmpty() && aforo.isNotEmpty()
                                ) {
                                    if (descripcion.length !in 5..15) {
                                        Toast.makeText(
                                            context,
                                            errorDescripcion,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }

                                    if (latitud == 0.0 || longitud == 0.0) {
                                        Toast.makeText(context, errorUbicacion, Toast.LENGTH_SHORT)
                                            .show()
                                        return@Button
                                    }

                                    val event = EventEntity(
                                        nombre = nombre,
                                        nombreOrg = nomOrg,
                                        categoria = categoriaSeleccionada,
                                        subcategoria = subcategoria,
                                        descripcion = descripcion,
                                        lugar = lugar,
                                        latitud = latitud,
                                        longitud = longitud,
                                        fechaInicio = parseDate(fechaInicio),
                                        fechaFin = parseDate(fechaFin),
                                        precio = precio.toDouble(),
                                        aforo = aforo.toInt(),
                                        idRealizador = currentUserId ?: ""
                                    )

                                    val photosToUpload = selectedPhotos.filterNotNull()
                                    viewModel.createEvent(event, photosToUpload) { createdEventId ->
                                        navController.navigate("map_screen") {
                                            popUpTo("map_screen") { inclusive = false }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, errorCampos, Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(id = R.string.crear), color = TurkBlue)
                        }
                    }
                }
            }
        }
    }

@Composable
fun CategoriaDropdown(selectedCategoria: TipoCategoria, onCategoriaSelected: (TipoCategoria) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    // Usa Row para permitir el uso de weight()
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.weight(1f)) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.2f))
            ) {
                Text(selectedCategoria.displayName.orEmpty(), color = Color.White)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.Black)
            ) {
                TipoCategoria.values().forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria.displayName.orEmpty(), color = Color.White) },
                        onClick = {
                            onCategoriaSelected(categoria)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// Función para convertir String a Date
fun parseDate(dateStr: String): Date {
    return try {
        val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(dateStr) ?: Date()
    } catch (e: Exception) {
        Date()
    }
}

@Composable
fun DateTimePickerField(
    label: String,
    dateTime: String,
    onDateTimeSelected: (String) -> Unit,
    minDate: Calendar? = null // Parámetro opcional para establecer una restricción mínima.
) {
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

                            // Verificar si la fecha seleccionada es anterior a la mínima permitida
                            if (minDate != null && selectedDate.before(minDate)) {
                                Toast.makeText(
                                    context,
                                    "La fecha final no puede ser anterior a la fecha inicial.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@TimePickerDialog
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
            ).apply {
                minDate?.let { datePicker.minDate = it.timeInMillis } // Establecer la restricción mínima en el selector de fecha
            }.show()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Text(
            text = if (dateTime.isEmpty()) label else dateTime,
            color = YellowLight
        )
    }
}

