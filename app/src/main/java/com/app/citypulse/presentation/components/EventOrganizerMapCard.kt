package com.app.citypulse.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@Composable
fun EventOrganizerMapCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        EventImageGallery(
            images = listOf(
                R.drawable.sample_party,
                R.drawable.sample_cena,
                R.drawable.sample_cultura
            ),
            modifier = Modifier.height(120.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // INFORMACION DEL EVENTO

        Column {
            Text(
                text = "Full day with your friends",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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
            Text(
                text = "Techno . Fiesta",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))

            //Estado y apertura
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Abierto",
                    fontSize = 14.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ". Cierre : 6:00 (dom)",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // 🔹 Espacio antes de los botones

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            ActionButton(
                text = "Asistiré",
                icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = "Asistencia") },
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                text = "Entradas",
                icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Entradas") },
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                text = "Compartir",
                icon = { Icon(imageVector = Icons.Default.Share, contentDescription = "Compartir") },
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventOrganizerMapCardPreview() {
    EventOrganizerMapCard()
}

