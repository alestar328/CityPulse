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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.R
import com.app.citypulse.presentation.viewmodel.EventViewModel

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
        Column(modifier = Modifier.fillMaxSize()) {
            // Fondo con imagen oscurecida
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.barnaw),
                    contentDescription = "Imagen del evento",
                    modifier = Modifier.fillMaxSize().graphicsLayer { alpha = 0.7f },
                    colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.3f))
                )
            }

            // Texto debajo de la imagen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFBDBDBD))
                    .padding(16.dp)
            ) {
                Text(
                    text = it.nombre.uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${it.fechaInicio} - ${it.fechaFin}",
                    fontSize = 18.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    InfoBox(label = "Lugar", value = it.lugar, wide = true)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    InfoBox(label = "Descripción", value = it.descripcion, wide = true)
                    Spacer(modifier = Modifier.width(16.dp))
                    InfoBox(label = "Categoría", value = it.categoria, wide = true)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    InfoBox(label = "Aforo", value = it.aforo.toString())
                    InfoBox(label = "Precio", value = "${it.precio}€")
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text("Volver", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }
    } ?: Text("Cargando detalles del evento...", modifier = Modifier.padding(16.dp))
}

@Composable
fun InfoBox(label: String, value: String, wide: Boolean = false) {
    Column(
        modifier = Modifier
            .width(if (wide) 320.dp else 160.dp)
            .padding(8.dp)
    ) {
        Text(text = label, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(12.dp),
                fontSize = 18.sp
            )
        }
    }
}
