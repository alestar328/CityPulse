package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.data.model.EventUiModel

@Composable
fun EventCard(
    event: EventUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val parts = event.fechaInicio.split(" ")
    val day = parts.getOrNull(0) ?: ""
    val mon = parts.getOrNull(1)?.uppercase() ?: ""

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Black, shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            )
            {
                Column(
                    //Fecha
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    //Dia
                    Text(
                        day,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center,

                        )
                    //Mes
                    Text(
                        mon,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.Center,

                        )
                }
            }
            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre el cuadro y el texto

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    //Nombre evento
                    text = event.nombre,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                Text(
                    //Creador evento
                    text = event.nomOrg,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row {
                        // 5 estrellas, rellenas u outline según la valoración
                        for (i in 1..5) {
                            val icon = if (i <= event.valoracion) {
                                Icons.Filled.Star
                            } else {
                                Icons.Outlined.Star
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 2.dp),
                                tint = Color(0xFFFFD700) // dorado
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        //Categoria evento
                        text = event.categoria.displayName ?: event.categoria.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        //Categoria evento
                        text = event.subcategoria,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray

                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun EventCardPreview() {
    val sample = EventUiModel(
        id              = "1",
        nombre          = "Carnaval Catalán",
        nomOrg          = "Razzmatazz",
        categoria       = TipoCategoria.FIESTA,
        subcategoria    = "Disfraces",
        descripcion     = "",
        lugar           = "C. Mónaco 196",
        latitud         = 0.0,
        longitud        = 0.0,
        fechaInicio     = "22 Feb 2025, 19:00",
        fechaFin        = "22 Feb 2025, 23:30",
        aforo           = 500,
        precio          = 20.0,
        valoracion      = 4,
        idRealizador    = "333333",
        galleryPictureUrls = emptyList()
    )
    EventCard(event = sample)
}