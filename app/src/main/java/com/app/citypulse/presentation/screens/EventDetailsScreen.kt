package com.app.citypulse.presentation.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.citypulse.data.model.EventUiModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.app.citypulse.R
import com.app.citypulse.data.enums.TipoCategoria

@Composable
fun EventDetailsScreen(event: EventUiModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(Color(0xFFFFF59D)),
        contentPadding = PaddingValues(
            bottom = 60.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1️⃣ Imagen de portada
        item {
            Box(
                modifier = Modifier
                    .height(500.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFA1887F))
                    .padding(horizontal = 45.dp),
                contentAlignment = Alignment.Center // Para centrar la imagen

            ) {
                Image(
                    painter = painterResource(id = R.drawable.eventimagecard),
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop // Recorta la imagen si sobrepasa el contenedor
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF59D)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "${event.fechaInicio} - ${event.fechaFin}",
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = event.nombre,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.categoria.displayName ?: event.categoria.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = event.subcategoria,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray // Ajusta el color según tus necesidades
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "La Fira Villarroel",
                    fontSize = 20.sp
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = event.lugar, fontSize = 18.sp)
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(text = "Aforo:", fontSize = 20.sp) //Organizador
                    Text(text = "250", fontSize = 20.sp) //Organizador

                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Precio:", fontSize = 18.sp) //Organizador
                    Text(text = "10 €", fontSize = 18.sp)

                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = event.descripcion, fontSize = 20.sp)
            }
        }
    }

}

@Preview
@Composable
fun EventDetailsScreenPreview() {
    val sampleEvent = EventUiModel(
        id = "1",
        nombre = "Carnaval Catalán",
        categoria = TipoCategoria.FIESTA,
        subcategoria = "Disfraces",
        descripcion = "Un concierto de prueba para el instituto jajajajja",
        lugar = "C. Monaco 196 , 08204 Barcelona, Spain",
        latitud = 0.0,
        longitud = 0.0,
        fechaInicio = "Sab, 22 feb 2025 19:00",
        fechaFin = "Sab, 22 feb 2025 23:30",
        aforo = 500,
        precio = 20.0,
        valoracion = 4
    )

    EventDetailsScreen(event = sampleEvent)
}