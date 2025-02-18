package com.app.citypulse.presentation.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

@Composable
fun EventDetailsScreen(event: EventUiModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Zona superior (2 partes)
        Box(
            modifier = Modifier
                .weight(2f)               // Peso proporcional de 2
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
            )        }

        // Zona inferior (1 parte)
        Box(
            modifier = Modifier
                .weight(1.2f)               // Peso proporcional de 1
                .fillMaxWidth()
                .background(Color(0xFFFFF59D))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Row(
                    modifier = Modifier.
                    fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "${event.fechaInicio} - ${event.fechaFin}",
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = event.nombre, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                }
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
                        fontSize = 25.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = event.lugar, fontSize = 23.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Text(text = "Aforo:", fontSize = 22.sp) //Organizador
                        Text(text = "250", fontSize = 22.sp) //Organizador

                    }
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "Precio:", fontSize = 22.sp) //Organizador
                        Text(text = "10 €", fontSize = 22.sp)

                    }
                }
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
        categoria = "Fiesta",
        subcategoria = "Disfraces",
        descripcion = "Un concierto de prueba",
        lugar = "C. Monaco 196 , 08204 Barcelona, Spain",
        latitud = 0.0,
        longitud = 0.0,
        fechaInicio = "Sab, 22 feb 2025 19:00",
        fechaFin = "Sab, 22 feb 2025 23:30",
        aforo = 500,
        precio = 20.0,
        valoracion = 4,
        idRealizador = 101
    )

    EventDetailsScreen(event = sampleEvent)
}