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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.citypulse.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventOrganizerMapCard(
    nombre: String,
    categoria: String,      // Ahora es un String ya formateado
    subcategoria: String?,
    lugar: String,
    fechaInicio: String,    // Fecha ya formateada
    fechaFin: String,       // Fecha ya formateada
    precio: Double,
    aforo: Int,


    images: List<Int> = listOf(
        R.drawable.sample_party,
        R.drawable.sample_cena,
        R.drawable.sample_cultura
    )
){

    val dateFormat = SimpleDateFormat("HH:mm (EEE)", Locale.getDefault())

    Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            EventImageGallery(images = images, modifier = Modifier.height(120.dp))

            Spacer(modifier = Modifier.height(12.dp))

        // INFORMACION DEL EVENTO

            Column {
                Text(text = nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(4.dp))


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
                // CATEGORIA
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

                //Estado y apertura
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
            Spacer(modifier = Modifier.height(7.dp)) // 🔹 Espacio antes de los botones

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
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Asistencia"
                        )
                    },
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Entradas",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Entradas"
                        )
                    },
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Compartir",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir"
                        )
                    },
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun EventOrganizerMapCardPreview() {
    val sampleDate = Date()

    EventOrganizerMapCard(
        nombre = "Full day with your friends",
        categoria = "Fiesta",
        subcategoria = "Techno",
        aforo = 666,
        fechaInicio = "18:00 (Wed)",    // Fecha preformateada
        fechaFin = "23:00 (Wed)",
        precio = 50.5,
        lugar = "Calle Paris 123, Barcelona"
    )
}
