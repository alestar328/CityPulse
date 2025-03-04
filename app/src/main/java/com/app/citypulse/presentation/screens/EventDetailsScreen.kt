package com.app.citypulse.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventDetailsScreen(
    eventId: String,
    viewModel: EventViewModel,
    navController: NavController,
    innerPadding: PaddingValues
) {
    val event by viewModel.eventFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getEventById(eventId)
    }
    LaunchedEffect(event) {
        event?.galleryPictureUrls?.forEach { url ->
            println("🔗 URL de la imagen: $url")
        }
    }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val isCreator = event?.idRealizador == currentUserId

    if (event == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Detalles del Evento",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            // 🔹 Carrusel de imágenes en lugar de imagen fija
            if (!event!!.galleryPictureUrls.isNullOrEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(event!!.galleryPictureUrls) { imageUrl ->
                        Box(
                            modifier = Modifier
                                .width(250.dp)
                                .height(250.dp)
                                .background(Color.Gray), // Fondo gris mientras carga
                            contentAlignment = Alignment.Center
                        ) {
                            val painter = rememberAsyncImagePainter(imageUrl)
                            Image(
                                painter = painter,
                                contentDescription = "Imagen del evento",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )
                            // Si hay un error al cargar la imagen, mostrar un mensaje
                            if (painter.state is coil.compose.AsyncImagePainter.State.Error) {
                                Text(
                                    text = "Error al cargar imagen",
                                    color = Color.Red,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            } else {
                Text("No hay imágenes disponibles", color = Color.White, fontSize = 16.sp)
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    EventDetailBox("Nombre", event!!.nombre, Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox(
                        "Hora Inicial",
                        event!!.fechaInicio?.let { dateFormat.format(it) } ?: "Sin fecha",
                        Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox(
                        "Hora Final",
                        event!!.fechaFin?.let { dateFormat.format(it) } ?: "Sin fecha",
                        Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    EventDetailBox("Lugar", event!!.lugar, Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox(
                        "Categoría",
                        event!!.categoria?.toString() ?: "Sin categoría",
                        Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                EventDetailBox(
                    "Descripción",
                    event!!.descripcion,
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    EventDetailBox("Precio", event!!.precio.toString(), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox("Aforo", event!!.aforo.toString(), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox("Subcategoría", "Mediterránea", Modifier.weight(1f))
                }
            }
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item {
            if (isCreator) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController.navigate("edit_event/$eventId") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                    ) {
                        Text("Editar", color = Color.White)
                    }

                    Button(
                        onClick = { viewModel.deleteEvent(eventId, navController) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text("Eliminar", color = Color.White)
                    }
                }
            }
        }
        item {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}
    @Composable
    fun EventDetailBox(label: String, value: String, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .padding(4.dp)
        ) {
            Text(label, color = Color.White, fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(12.dp) // Más padding para que se vea mejor
            ) {
                Text(value, color = Color.Blue, fontSize = 16.sp)
            }
        }
    }