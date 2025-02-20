package com.app.citypulse.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.app.citypulse.R // Asegúrate de tener una imagen en res/drawable/placeholder.png

@Composable
fun EventDetailsScreen(
    eventId: String,
    viewModel: EventViewModel,
    navController: NavController
) {
    LaunchedEffect(eventId) {
        viewModel.getEventById(eventId)
    }

    val event = viewModel.eventDetails.value

    event?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔥 Imagen ocupa toda la parte superior sin bordes grises
            Image(
                painter = painterResource(id = R.drawable.barnaw),
                contentDescription = "Imagen del evento",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp), // Ajusta la altura si quieres que ocupe más
                contentScale = ContentScale.Crop // Recorta la imagen para que ocupe toda la anchura sin bordes
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🏗️ Organiza los datos en bloques que llenen la pantalla
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp)) {

                // 🔷 Nombre y Horario
                Row(modifier = Modifier.fillMaxWidth()) {
                    EventDetailBox("Nombre", it.nombre, Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox("Hora Inicial", it.fechaInicio, Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox("Hora Final", it.fechaFin, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    EventDetailBox("Lugar", it.lugar, Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox("Categoría", it.categoria, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 📜 Descripción (ocupa todo el ancho)
                EventDetailBox("Descripción", it.descripcion, Modifier.fillMaxWidth().weight(1f))

                Spacer(modifier = Modifier.height(8.dp))

                // 💰 Precio, Aforo y Subcategoría
                Row(modifier = Modifier.fillMaxWidth()) {
                    EventDetailBox("Precio", it.precio.toString(), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox("Aforo", it.aforo.toString(), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox("Subcategoría", "Mediterránea", Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔘 Botón de volver (fijo abajo)
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
    } ?: Text("Cargando detalles del evento...", color = Color.White)
}

// 📝 Componente reutilizable para los campos de información
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
