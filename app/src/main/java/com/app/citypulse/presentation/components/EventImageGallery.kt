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
import coil.compose.rememberAsyncImagePainter
import com.app.citypulse.R

@Composable
fun EventImageGallery(
    modifier: Modifier = Modifier,
    images: List<String> // Lista de imágenes (en un caso real, sería una URL)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado uniforme entre imágenes

    ){
        images.take(3).forEach { imageUrl ->
            val painter = if (imageUrl.startsWith("http")) {
                rememberAsyncImagePainter(imageUrl)
            } else {
                painterResource(id = R.drawable.sample_party) // Imagen de respaldo si es local
            }

            Image(
                painter = painter,
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



