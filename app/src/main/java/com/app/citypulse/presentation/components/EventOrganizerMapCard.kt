package com.app.citypulse.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EventOrganizerMapCard(
    nombre: String,
    nomOrg: String,
    categoria: String,
    subcategoria: String?,
    lugar: String,
    fechaInicio: String,
    fechaFin: String,
    precio: Double,
    aforo: Int,
    eventId: String,
    navController: NavController,
    images: List<String>,
    onSaved: () -> Unit,
    onAssisted:() -> Unit
) {
    var isGoing by remember { mutableStateOf(false) }
    var isInterested by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        EventImageGallery(images = images, modifier = Modifier.height(120.dp))

        Spacer(modifier = Modifier.height(12.dp))

        Column {
            Text(text = nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(1.dp))
            Text(text = nomOrg, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrella",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "4.3(2,666)",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(text = categoria, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                subcategoria?.let {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = it, fontSize = 14.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("Aforo: $aforo", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Empieza",
                    fontSize = 14.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = fechaInicio, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Termina",
                    fontSize = 14.sp,
                    color = Color(0xFFFF0606),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = fechaFin, fontSize = 14.sp, color = Color.Gray)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ubicación: $lugar", fontSize = 14.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(7.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            ActionButton(
                text = "Asistiré",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Asistiré"
                    )
                },
                onClick = { isGoing = !isGoing
                          onAssisted()
                          },
                modifier = Modifier.weight(1f),
                backgroundColor = if (isGoing) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                contentColor = if (isGoing) Color.White else Color.Black

            )
            ActionButton(
                text = "Guardar",
                onClick = {
                    isInterested = !isInterested
                    onSaved()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Asistencia"
                    )
                },
                modifier = Modifier.weight(1f),
                backgroundColor = if (isInterested) Color(0xFFE91E63) else Color(0xFFE0E0E0),
                contentColor = if (isInterested) Color.White else Color.Black
            )
            // 🔹 Nuevo botón de "Información"
            ActionButton(
                text = "Info",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Información"
                    )
                },
                onClick = { navController.navigate("event_details/$eventId") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}




