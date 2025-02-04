package com.app.citypulse.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.citypulse.R

@Composable
fun EventImageGallery(
    modifier: Modifier = Modifier, // Permite personalizar el tamaño según dónde se use
    images: List<Int> // Lista de imágenes (en un caso real, sería una URL)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado uniforme entre imágenes

    ){
        images.forEach{ imageRes ->
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Imagen del evento",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            )

        }

    }

}


@Preview
@Composable
fun EventImageGalleryPreview() {
    EventImageGallery(
        images = listOf(R.drawable.sample_party,R.drawable.sample_cena,R.drawable.sample_cultura )
    )
}