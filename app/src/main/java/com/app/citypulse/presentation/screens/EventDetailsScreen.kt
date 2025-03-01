package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.citypulse.presentation.viewmodel.EventViewModel

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

    val event = viewModel.eventDetails.value
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val isCreator = event?.idRealizador == currentUserId

    event?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.barnaw),
                contentDescription = "Imagen del evento",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp)) {

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

                EventDetailBox("Descripción", it.descripcion, Modifier.fillMaxWidth().weight(1f))

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    EventDetailBox("Precio", it.precio.toString(), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox("Aforo", it.aforo.toString(), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    EventDetailBox("Subcategoría", "Mediterránea", Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isCreator) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /* Navegar a la pantalla de edición */ },
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
